package cn.xuank.pojo;

public class Biao {
    private Integer id;

    private String name;

    private Integer age;

    private String biaocol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBiaocol() {
        return biaocol;
    }

    public void setBiaocol(String biaocol) {
        this.biaocol = biaocol == null ? null : biaocol.trim();
    }
}