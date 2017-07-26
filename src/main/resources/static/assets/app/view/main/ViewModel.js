/**
 * Created by micro on 2017/2/10.
 */
Ext.define('Demo.view.main.ViewModel',{
    extend:'Ext.app.ViewModel',
    alias: 'viewmodel.mainViewport',
    stores:{
        navItems: {
            type: 'tree',
            autoLoad:true,
            proxy: {
                type: 'ajax',
                url: '/tree'
            }
        }
    }
});