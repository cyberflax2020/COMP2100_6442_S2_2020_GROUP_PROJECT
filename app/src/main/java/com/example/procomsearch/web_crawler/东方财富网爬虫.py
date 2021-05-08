/**
 * Author:Yuchen Wanf
 */

  1from selenium import webdriver
  2from selenium.common.exceptions import TimeoutException
  3from selenium.webdriver.common.by import By
  4from selenium.webdriver.support import expected_conditions as EC
  5from selenium.webdriver.support.wait import WebDriverWait
  6import time
  7import pandas as pd
  8import os
  9
 10# 先chrome，后phantomjs
 11# browser = webdriver.Chrome()
 12# 添加无头headlesss
 13chrome_options = webdriver.ChromeOptions()
 14chrome_options.add_argument('--headless')
 15browser = webdriver.Chrome(chrome_options=chrome_options)
 16
 17# browser = webdriver.PhantomJS() # 会报警高提示不建议使用phantomjs，建议chrome添加无头
 18browser.maximize_window()  # 最大化窗口
 19wait = WebDriverWait(browser, 10)
 20
 21def index_page(page):
 22    try:
 23        print('正在爬取第： %s 页' % page)
 24        wait.until(
 25            EC.presence_of_element_located((By.ID, "dt_1")))
 26        # 判断是否是第1页，如果大于1就输入跳转，否则等待加载完成。
 27        if page > 1:
 28            # 确定页数输入框
 29            input = wait.until(EC.presence_of_element_located(
 30                (By.XPATH, '//*[@id="PageContgopage"]')))
 31            input.click()
 32            input.clear()
 33            input.send_keys(page)
 34            submit = wait.until(EC.element_to_be_clickable(
 35                (By.CSS_SELECTOR, '#PageCont > a.btn_link')))
 36            submit.click()
 37            time.sleep(2)
 38        # 确认成功跳转到输入框中的指定页
 39        wait.until(EC.text_to_be_present_in_element(
 40            (By.CSS_SELECTOR, '#PageCont > span.at'), str(page)))
 41    except Exception:
 42        return None
 43
 44def parse_table():
 45    # 提取表格第一种方法
 46    # element = wait.until(EC.presence_of_element_located((By.ID, "dt_1")))
 47    # 第二种方法
 48    element = browser.find_element_by_css_selector('#dt_1')
 49
 50    # 提取表格内容td
 51    td_content = element.find_elements_by_tag_name("td")
 52    lst = []
 53    for td in td_content:
 54        # print(type(td.text)) # str
 55        lst.append(td.text)
 56
 57    # 确定表格列数
 58    col = len(element.find_elements_by_css_selector('tr:nth-child(1) td'))
 59    # 通过定位一行td的数量，可获得表格的列数，然后将list拆分为对应列数的子list
 60    lst = [lst[i:i + col] for i in range(0, len(lst), col)]
 61
 62    # 原网页中打开"详细"链接，可以查看更详细的数据，这里我们把url提取出来，方便后期查看
 63    lst_link = []
 64    links = element.find_elements_by_css_selector('#dt_1 a.red')
 65    for link in links:
 66        url = link.get_attribute('href')
 67        lst_link.append(url)
 68
 69    lst_link = pd.Series(lst_link)
 70    # list转为dataframe
 71    df_table = pd.DataFrame(lst)
 72    # 添加url列
 73    df_table['url'] = lst_link
 74
 75    # print(df_table.head())
 76    return df_table
 77
 78# 写入文件
 79def write_to_file(df_table, category):
 80    # 设置文件保存在D盘eastmoney文件夹下
 81    file_path = 'D:\\eastmoney'
 82    if not os.path.exists(file_path):
 83        os.mkdir(file_path)
 84    os.chdir(file_path)
 85    df_table.to_csv('{}.csv' .format(category), mode='a',
 86                    encoding='utf_8_sig', index=0, header=0)
 87
 88# 设置表格获取时间、类型
 89def set_table():
 90    print('*' * 80)
 91    print('\t\t\t\t东方财富网报表下载')
 92    print('作者：高级农民工  2018.10.6')
 93    print('--------------')
 94
 95    # 1 设置财务报表获取时期
 96    year = int(float(input('请输入要查询的年份(四位数2007-2018)：\n')))
 97    # int表示取整，里面加float是因为输入的是str，直接int会报错，float则不会
 98    # https://stackoverflow.com/questions/1841565/valueerror-invalid-literal-for-int-with-base-10
 99    while (year < 2007 or year > 2018):
100        year = int(float(input('年份数值输入错误，请重新输入：\n')))
101
102    quarter = int(float(input('请输入小写数字季度(1:1季报，2-年中报，3：3季报，4-年报)：\n')))
103    while (quarter < 1 or quarter > 4):
104        quarter = int(float(input('季度数值输入错误，请重新输入：\n')))
105
106    # 转换为所需的quarter 两种方法,2表示两位数，0表示不满2位用0补充，
107    # http://www.runoob.com/python/att-string-format.html
108    quarter = '{:02d}'.format(quarter * 3)
109    # quarter = '%02d' %(int(month)*3)
110    date = '{}{}' .format(year, quarter)
111    # print(date) 测试日期 ok
112
113    # 2 设置财务报表种类
114    tables = int(
115        input('请输入查询的报表种类对应的数字(1-业绩报表；2-业绩快报表：3-业绩预告表；4-预约披露时间表；5-资产负债表；6-利润表；7-现金流量表): \n'))
116
117    dict_tables = {1: '业绩报表', 2: '业绩快报表', 3: '业绩预告表',
118                   4: '预约披露时间表', 5: '资产负债表', 6: '利润表', 7: '现金流量表'}
119    dict = {1: 'yjbb', 2: 'yjkb/13', 3: 'yjyg',
120            4: 'yysj', 5: 'zcfz', 6: 'lrb', 7: 'xjll'}
121    category = dict[tables]
122
123    # 3 设置url
124    # url = 'http://data.eastmoney.com/bbsj/201803/lrb.html' eg.
125    url = 'http://data.eastmoney.com/{}/{}/{}.html' .format(
126        'bbsj', date, category)
127
128    # # 4 选择爬取页数范围
129    start_page = int(input('请输入下载起始页数：\n'))
130    nums = input('请输入要下载的页数，（若需下载全部则按回车）：\n')
131    print('*' * 80)
132
133    # 确定网页中的最后一页
134    browser.get(url)
135    # 确定最后一页页数不直接用数字而是采用定位，因为不同时间段的页码会不一样
136    try:
137        page = browser.find_element_by_css_selector('.next+ a')  # next节点后面的a节点
138    except:
139        page = browser.find_element_by_css_selector('.at+ a')
140    # else:
141    #     print('没有找到该节点')
142    # 上面用try.except是因为绝大多数页码定位可用'.next+ a'，但是业绩快报表有的只有2页，无'.next+ a'节点
143    end_page = int(page.text)
144
145    if nums.isdigit():
146        end_page = start_page + int(nums)
147    elif nums == '':
148        end_page = end_page
149    else:
150        print('页数输入错误')
151    # 输入准备下载表格类型
152    print('准备下载:{}-{}' .format(date, dict_tables[tables]))
153    print(url)
154    yield{
155        'url': url,
156        'category': dict_tables[tables],
157        'start_page': start_page,
158        'end_page': end_page
159    }
160
161def main(category, page):
162    try:
163        index_page(page)
164        # parse_table() #测试print
165        df_table = parse_table()
166        write_to_file(df_table, category)
167        print('第 %s 页抓取完成' % page)
168        print('--------------')
169    except Exception:
170        print('网页爬取失败，请检查网页中表格内容是否存在')
171# 单进程
172if __name__ == '__main__':
173
174    for i in set_table():
175        # url = i.get('url')
176        category = i.get('category')
177        start_page = i.get('start_page')
178        end_page = i.get('end_page')
179
180    for page in range(start_page, end_page):
181        # for page in range(44,pageall+1): # 如果下载中断，可以尝试手动更改网页继续下载
182        main(category, page)
183    print('全部抓取完成')