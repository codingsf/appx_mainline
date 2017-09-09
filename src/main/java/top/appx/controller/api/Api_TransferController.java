package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.appx.controller.BaseController;
import top.appx.service.TransferService;

@RestController
@RequestMapping("/api/transfer")
public class Api_TransferController extends BaseController {
    @Autowired
    private TransferService transferService;


    @GetMapping
    public Object list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "20") int pageSize)throws Exception{
        return transferService.findPage(pageNum,pageSize);
    }


}
