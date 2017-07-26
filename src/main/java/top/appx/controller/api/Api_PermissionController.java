package top.appx.controller.api;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.appx.controller.BaseController;
import top.appx.entity.Permission;
import top.appx.entity.PermissionType;
import top.appx.entity.User;
import top.appx.service.PermissionService;
import top.appx.util.ResponseMap;

import java.util.ArrayList;
import java.util.List;

@RestController("/api/permissions")
public class Api_PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/tree")
    public Object tree(){
        ResponseMap responseMap = new ResponseMap();
        responseMap.p("expanded",true);

        List<Permission> permissionList = permissionService.find();

        List<Object> children = new ArrayList<>();
        for (Permission permission : permissionList) {
            if(permission.getType()== 1){
                ResponseMap rm = new ResponseMap();
                rm.put("leaf",false);
                rm.put("text",permission.getName());
                rm.put("expanded",true);
                List<Object> children1 = new ArrayList<>();
                for (Permission permission1 : permissionList) {
                    if(permission1.getParentId()==permission.getId()){
                        children1.add(ResponseMap.instance().p("text",permission1.getName()).p("leaf",true).p("url",permission1.getUrl()));
                    }
                }
                rm.put("children",children1);
                children.add(rm);
            }
        }

        return responseMap.p("children",children);

    }
}
