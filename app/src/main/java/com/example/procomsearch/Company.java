package com.example.procomsearch;


import java.io.Serializable;
import java.util.Objects;

/**
 * @author Chaofan Li
 */
public class Company implements Serializable {
    private String name;
    private String stockNo;
    private int image_id;
    public boolean liked;
    private double promoted;

    public Company(String name, String stockNo, String url, double promoted) {
        this.name = name;
        this.stockNo = stockNo;
        setImage();
        this.promoted = promoted;
    }

    public double getPromoted() {
        return promoted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStockNo() {
        return stockNo;
    }

    public int getImage_id() {
        return image_id;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", stockNo='" + stockNo + '\'' +
                ", liked="+liked+'\''+
                ", image_id=" + image_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return stockNo.equals(company.stockNo);
    }

    private void setImage(){
        int imageNo = Math.abs(hashCode()%10);
        switch (imageNo){
            case 0:
                this.image_id = R.drawable.angry;
                break;
            case 1:
                this.image_id = R.drawable.boo;
                break;
            case 2:
                this.image_id = R.drawable.meaw;
                break;
            case 3:
                this.image_id = R.drawable.brains;
                break;
            case 4:
                this.image_id = R.drawable.dizzy;
                break;
            case 5:
                this.image_id = R.drawable.evilish;
                break;
            case 6:
                this.image_id = R.drawable.haha;
                break;
            case 7:
                this.image_id = R.drawable.graffiti;
                break;
            case 8:
                this.image_id = R.drawable.disappearing;
                break;
            case 9:
                this.image_id = R.drawable.grin;
                break;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stockNo, image_id, liked);
    }
}
