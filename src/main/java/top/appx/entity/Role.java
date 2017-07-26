package top.appx.entity;

import javax.validation.constraints.NotNull;

public class Role extends BaseEntity {

    private Long id;
    /**
     * 角色名
     */
    @NotNull(message = "角色不能为空!")
    private String name;
    @NotNull(message = "角色标识不能为空")
    private String perms;
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
