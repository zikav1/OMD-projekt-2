package xl.model;

public class Status {
    
    private String status;

    public Status(String status){
        this.status = status;
    }

    public void clearStatus(){
        status = "";
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String s){
        status = s;
    }
}
