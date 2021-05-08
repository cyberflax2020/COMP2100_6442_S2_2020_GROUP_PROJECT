package com.example.procomsearch;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Chaofan Li
 */
public class CompanyFactory extends ArrayList<Company> implements Serializable {

    @Override
    public boolean add(Company company) {
        return super.add(company);
    }

    @Override
    public Company remove(int index) {
        return super.remove(index);
    }

    @Override
    public Company get(int index) {
        return super.get(index);
    }

    public boolean contains( Company company) {
        for (Company c:this
             ) {
            if(c.equals(company)){
                return true;
            }
        }
        return false;
    }

    public int count(){
        return this.size();
    }
}
