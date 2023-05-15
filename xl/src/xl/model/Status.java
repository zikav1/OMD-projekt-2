package xl.model;

public class Status {
    
    private String status;

    public Status(){
        this.status = "";
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
