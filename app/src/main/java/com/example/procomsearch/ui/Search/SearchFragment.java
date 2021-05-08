package com.example.procomsearch.ui.Search;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procomsearch.Company;
import com.example.procomsearch.CompanyAdapter;
import com.example.procomsearch.CompanyFactory;
import com.example.procomsearch.R;
import com.example.procomsearch.SearchActivity;
import com.example.procomsearch.SelectActivity;
import com.example.procomsearch.buttonEditText.ClearableEditText;
import com.example.procomsearch.dataFrame.ArrayListIntent;
import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.history.HistoryUtil;
import com.example.procomsearch.layoutManagerTool.AutoPageUpLinearLayoutManager;
import com.example.procomsearch.parser.Exp;
import com.example.procomsearch.parser.Parser;
import com.example.procomsearch.parser.UnknownRst;
import com.example.procomsearch.searcher.Searcher;
import com.example.procomsearch.tokenizer.Tokenizer;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * @author Chaofan Li
 */
public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private ClearableEditText searchBox;
    private Button searchButton;
    private ProgressBar progressBar;
    private int userId;
    private String rankAttribute = "NPA";
    DrawerLayout mDrawerLayout;
    View root;
    private ArrayList<String> textList;
    Toolbar toolbar;
    ImageButton keyBoard, hintButton, autoInput;
    static boolean firstTime = true;
    static boolean typeOddTimes = true;
    ListPopupWindow listPopupWindow = null;


    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //reference:https://blog.csdn.net/lvshuchangyin/article/details/73825933
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_search, container, false);


        toolbar = (Toolbar) root.findViewById(R.id.toolbar_search);
        toolbar.inflateMenu(R.menu.rank_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rank_npa: {
                        rankAttribute = "NPA";
                        break;
                    }
                    case R.id.rank_npg: {
                        rankAttribute = "NPG";
                        break;
                    }
                    case R.id.rank_toia: {
                        rankAttribute = "TOIA";
                        break;
                    }
                    case R.id.rank_toig: {
                        rankAttribute = "TOIG";
                        break;
                    }
                    case R.id.rank_oe: {
                        rankAttribute = "OE";
                        break;
                    }
                    case R.id.rank_se: {
                        rankAttribute = "SE";
                        break;
                    }
                    case R.id.rank_me: {
                        rankAttribute = "ME";
                        break;
                    }
                    case R.id.rank_fe: {
                        rankAttribute = "FE";
                        break;
                    }
                    case R.id.rank_op: {
                        rankAttribute = "OP";
                        break;
                    }
                    case R.id.rank_toe: {
                        rankAttribute = "TOE";
                        break;
                    }
                    case R.id.rank_tp: {
                        rankAttribute = "TP";
                        break;
                    }
                }
                return true;
            }
        });
        //change overFlow icon
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.rankkkk));

        keyBoard = (ImageButton) root.findViewById(R.id.keyboard);
        keyBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        SelectActivity activity = (SelectActivity) getActivity();

        mDrawerLayout = root.findViewById(R.id.drawerlayout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        //init new input textList
        textList = new ArrayList<>();

        userId = activity.getuserid();
        searchBox = (ClearableEditText) root.findViewById(R.id.search_text);
        searchButton = (Button) root.findViewById(R.id.search_btn);
        searchBox.setSingleLine(false);
        searchBox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        searchBox.setGravity(Gravity.BOTTOM);
        searchBox.setSingleLine(false);
        searchBox.setHorizontallyScrolling(false);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputSearch = searchBox.getText().toString().trim();
                search(inputSearch);
            }
        });

        //quick input
        setQuickInput();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBox.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        autoInput = (ImageButton) root.findViewById(R.id.autoinput_btn);
        autoInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = "((NPA>500)&(!(NPG<120)))&((TP>800)|(TOIG>170))";
                searchBox.setText(input);
                search(input);
//                searchBox.setText("(!(NPA<1300)&(!(NPG<7000)))");
//                searchBox.setText("(!(NPG<7000))");
//                searchBox.setText("(!(NPA<1300))");
//                searchBox.setText("!(NPA<103000)");
//                searchBox.setText("(!(NPG<7000))&(!(NPA<103000))");
//                searchBox.setText("(!(NPA<103000))&(!(NPG<7000))");
//                  searchBox.setText("((NPA>0)|(NPA<1000))");
            }
        });

        if (userId > 0) {
            searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        showListPopupWindow();
                    }
                }
            });
        }

        setMightBeInterest(root);

        hintButton = (ImageButton) root.findViewById(R.id.hint_button);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupHint(v);
            }
        });
        //if this is the first time to open this view, auto show the hint
        if (firstTime) {
            hintButton.post(new Runnable() {
                @Override
                public void run() {
                    hintButton.performClick();
                }
            });
        }

        firstTime = false;
        return root;
    }

    private void search(String inputSearch) {
        if (inputSearch == null || inputSearch.equals("")) {
            searchBox.setError("You need to enter conditions!");
        } else {

            ArrayListIntent AI = new ArrayListIntent(parseQuery(inputSearch, rankAttribute), inputSearch, userId);
            System.out.println("rank attribute111:" + rankAttribute);


            if ((AI.getList().size() == 0)) {
                searchBox.setError("There is no such companies");
            } else if (AI.getList().get(0).Name.equals("Wrong input")) {
                searchBox.setError("Invalid input!");
            } else {
                progressBar.setVisibility(View.VISIBLE);
                //add search text to searchHistory
                HistoryUtil.addSearchHistory(inputSearch, userId);

                Intent intent = new Intent(getActivity(), SearchActivity.class);

                intent.putExtra("search rst", AI);
                intent.putExtra("rank attributes", rankAttribute);
                System.out.println("rank attribute222:" + rankAttribute);
                printCompanies(AI);

                startActivity(intent);
            }
        }
    }


    private void setMightBeInterest(View root) {
        CompanyFactory companies = new CompanyFactory();
        ArrayList<Company_Index> promotedCompanies = Searcher.getAllPromotedSortedList();
        for (Company_Index c : promotedCompanies
        ) {
            companies.add(new Company(c.Name, c.getCode(), null, 1.0));
        }
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.promoted_coms);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        AutoPageUpLinearLayoutManager glm = new AutoPageUpLinearLayoutManager(getContext(), 1, false, recyclerView);
        recyclerView.setLayoutManager(glm);
        CompanyAdapter adapter = new CompanyAdapter(companies, null, null, userId);
        System.out.println("onCreate");
        recyclerView.setAdapter(adapter);
    }

    /**
     * searchbox popupwindow
     */
    private void showListPopupWindow() {
        HistoryUtil.readFromSearchHistory(userId);
        final ArrayList<String> list = HistoryUtil.getSearchHistories();
        list.add("Clear");
        final Object[] s = list.toArray();

        listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, s));
        listPopupWindow.setAnchorView(searchBox);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == s.length - 1) {
                    list.clear();
                    list.add("Clear");
                    HistoryUtil.clearHistory(userId, "SearchHistory");
                } else {
                    searchBox.setText(s[position] + "");
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    /**
     * quick input can be used to type in query quickly
     */
    private void setQuickInput() {
        final NavigationView navigationView = (NavigationView) root.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.npa_btn:
                        searchBox.append("NPA");
                        textList.add("NPA");
                        break;
                    case R.id.npg_btn:
                        searchBox.append("NPG");
                        textList.add("NPG");
                        break;
                    case R.id.toe_btn:
                        searchBox.append("TOE");
                        textList.add("TOE");
                        break;
                    case R.id.toia_btn:
                        searchBox.append("TOIA");
                        textList.add("TOIA");
                        break;
                    case R.id.toig_btn:
                        searchBox.append("TOIG");
                        textList.add("TOIG");
                        break;
                    case R.id.se_btn:
                        searchBox.append("SE");
                        textList.add("SE");
                        break;
                    case R.id.me_btn:
                        searchBox.append("ME");
                        textList.add("ME");
                        break;
                    case R.id.fe_btn:
                        searchBox.append("FE");
                        textList.add("FE");
                        break;
                    case R.id.op_btn:
                        searchBox.append("OP");
                        textList.add("OP");
                        break;
                    case R.id.tp_btn:
                        searchBox.append("TP");
                        textList.add("TP");
                        break;
                    case R.id.simbols_btn: {
                        Menu navMenu = navigationView.getMenu();
                        MenuItem menuItem = navMenu.findItem(R.id.simbols_btn);
                        View anchorView = menuItem.getActionView();
                        PopupMenu popupMenu = new PopupMenu(getContext(), anchorView);
                        popupMenu.inflate(R.menu.simbol_menu);
                        //make text center
                        String title = "()=><&|!";
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.left_pre: {
                                        searchBox.append("(");
                                        textList.add("(");
                                        break;
                                    }
                                    case R.id.right_pre: {
                                        searchBox.append(")");
                                        textList.add(")");
                                        break;
                                    }
                                    case R.id.equal: {
                                        searchBox.append("=");
                                        textList.add("=");
                                        break;
                                    }
                                    case R.id.bigger: {
                                        searchBox.append(">");
                                        textList.add(">");
                                        break;
                                    }
                                    case R.id.smaller: {
                                        searchBox.append("<");
                                        textList.add("<");
                                        break;
                                    }
                                    case R.id.and: {
                                        searchBox.append("&");
                                        textList.add("&");
                                        break;
                                    }
                                    case R.id.or: {
                                        searchBox.append("|");
                                        textList.add("|");
                                        break;
                                    }
                                    case R.id.not: {
                                        searchBox.append("!");
                                        textList.add("!");
                                        break;
                                    }
                                }
                                return false;
                            }


                        });
                        for (int i = 0; i < popupMenu.getMenu().size(); i++) {
                            SpannableString s = new SpannableString(title.charAt(i) + "");
                            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
                            popupMenu.getMenu().getItem(i).setTitle(s);
                        }
                        popupMenu.show();
                    }
                    break;

                    case R.id.digit_btn: {
                        Menu navMenu = navigationView.getMenu();
                        MenuItem menuItem = navMenu.findItem(R.id.digit_btn);
                        View anchorView = menuItem.getActionView();
                        final PopupMenu popupMenu = new PopupMenu(getContext(), anchorView);
                        popupMenu.inflate(R.menu.digits_menu);
                        //make text center
                        String title = "0123456789";
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.zero_btn: {
                                        searchBox.append("0");
                                        textList.add("0");
                                        break;
                                    }
                                    case R.id.one_btn: {
                                        searchBox.append("1");
                                        textList.add("1");
                                        break;
                                    }
                                    case R.id.two_btn: {
                                        searchBox.append("2");
                                        textList.add("2");
                                        break;
                                    }
                                    case R.id.three_btn: {
                                        searchBox.append("3");
                                        textList.add("3");
                                        break;
                                    }
                                    case R.id.four_btn: {
                                        searchBox.append("4");
                                        textList.add("4");
                                        break;
                                    }
                                    case R.id.five_btn: {
                                        searchBox.append("5");
                                        textList.add("5");
                                        break;
                                    }
                                    case R.id.six_btn: {
                                        searchBox.append("6");
                                        textList.add("6");
                                        break;
                                    }
                                    case R.id.seven_btn: {
                                        searchBox.append("7");
                                        textList.add("7");
                                        break;
                                    }
                                    case R.id.eight_btn: {
                                        searchBox.append("8");
                                        textList.add("8");
                                        break;
                                    }
                                    case R.id.nine_btn: {
                                        searchBox.append("9");
                                        textList.add("9");
                                        break;
                                    }
                                }
                                popupMenu.show();
                                return false;
                            }


                        });
                        for (int i = 0; i < popupMenu.getMenu().size(); i++) {
                            SpannableString s = new SpannableString(title.charAt(i) + "");
                            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
                            popupMenu.getMenu().getItem(i).setTitle(s);
                        }
                        popupMenu.show();
                        break;
                    }
                    case R.id.backspace_btn:
                        searchBox.setText("");
                        if (textList.size() != 0) {
                            textList.remove(textList.size() - 1);
                        }
                        for (String s : textList
                        ) {
                            searchBox.append(s);
                        }
                        break;
                }


                return false;
            }
        });
    }


    /**
     * @param v view
     * @Author Chaofan Li
     * @Reference https://www.runoob.com/w3cnote/android-tutorial-popupwindow.html
     */
    private void showPopupHint(View v) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hint_popup, null, false);
        TextView hintContent = (TextView) view.findViewById(R.id.hint_content);
        hintContent.setText(R.string.SerachHint);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x9597605E));
        popupWindow.showAsDropDown(v, 100, -70);
    }


    private static ArrayList<Company_Index> parseQuery(String query, String RankAttribute) {
/**
 * Author:Chaofan Li, Yuliang Ma
 */
        //点击search之后开始运行
        Tokenizer T = new Tokenizer(query);
        //search之后运行
        Exp e = new Parser(T).LogicalExp1();

        if (T.checkBracketMatching()) {

            ArrayList<Company_Index> rst = new ArrayList<>();
            rst.addAll(e.evaluate());

            ArrayList<Company_Index> sortedRst = new ArrayList<>();
            sortedRst.addAll(Searcher.sortCompanies(rst, RankAttribute));


            return sortedRst;
        } else
            return UnknownRst.getRst();
    }


    public static void printCompanies(ArrayListIntent AI) {


        ArrayList<Company_Index> rst = AI.getList();
        for (Company_Index c : rst
        ) {
            System.out.println("Name: " + c.Name);
            System.out.println("Key: " + c.Key);
            System.out.println("Promoted Power: " + c.getPromotedPower());
        }
    }
}
