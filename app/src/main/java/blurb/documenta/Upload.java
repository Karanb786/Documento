package blurb.documenta;

public class Upload {
    private String mName;
    private String ImgUrl;

    public Upload(){

    }
    public Upload(String name,String imageUrl){

        if(name.trim().equals("")){
            name="No name";

        }

        mName=name;
        ImgUrl=imageUrl;


    }
    public String getName(){
        return mName;

    }
    public  void setName(String name){
mName=name;
    }
    public String getImageUrl(){
        return ImgUrl;



        }
    public void setImgurl(String imgurl){
        ImgUrl=imgurl;
    }

}
