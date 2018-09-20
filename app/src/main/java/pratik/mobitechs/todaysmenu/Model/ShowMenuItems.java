package pratik.mobitechs.todaysmenu.Model;

public class ShowMenuItems {

    public String Userid;
    public String MenuType;
    public String date;
    public String Food;
    public String FoodRemark;
    public String CookingFoodRemark;
    public String menuId;
    public String name;

    public ShowMenuItems() {
    }

    public ShowMenuItems(String Userid, String MenuType, String date, String Food ,String FoodRemark ,String CookingFoodRemark,String menuId,String name) {

        this.Userid = Userid;
        this.date = date;
        this.MenuType = MenuType;
        this.Food = Food;
        this.FoodRemark = FoodRemark;
        this.CookingFoodRemark = CookingFoodRemark;
        this.menuId = menuId;
        this.name = name;

    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public String getMenuType() {
        return MenuType;
    }

    public void setMenuType(String MenuType) {
        this.MenuType = MenuType;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getFood() {
        return Food;
    }
    public void setFood(String Food) {
        this.Food = Food;
    }

    public String getLFoodRemark() {
        return FoodRemark;
    }
    public void setLFoodRemark(String FoodRemark) {
        this.FoodRemark = FoodRemark;
    }

    public String getCookingFoodRemark() {
        return CookingFoodRemark;
    }
    public void setCookingFoodRemark(String CookingFoodRemark) {
        this.CookingFoodRemark = CookingFoodRemark;
    }

    public String getmenuId() {
        return menuId;
    }
    public void setmenuId(String menuId) {
        this.menuId = menuId;
    }


    public String getnmae() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }


}
