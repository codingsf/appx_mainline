/**
 * Created by micro on 2017/2/10.
 */
Ext.define('Demo.view.main.Viewport', {
    //extend: 'Ext.container.Viewport',
    requires:['Demo.view.main.ViewModel',"Demo.view.main.Nav"],
    extend: 'Ext.panel.Panel',
    layout: 'border',
    title: 'TreeList',
    iconCls: 'fa fa-gears',
    //viewModel:'mainViewport',
    viewModel:{
        type:'mainViewport'
    },
    header: {//头部
        items: [{
            xtype: 'button',
            text: 'Options',
            menu: [{
                text: 'Expander Only',
                checked: true,
                config: 'expanderOnly'
            }, {
                text: 'Single Expand',
                checked: false,
                //handler: 'onToggleConfig',
                config: 'singleExpand'
            }]
        },{
            xtype: 'button',
            text: 'Nav',
            enableToggle: true,
            reference: 'navBtn',
            //toggleHandler: 'onToggleNav'
        },{
            xtype: 'button',
            text: 'Micro',
            enableToggle: true,
            onClick:function(){
                alert("aaa")
            }
            //toggleHandler: 'onToggleMicro'
        }]
    },
    items: [{
        region: 'west',
        width: 250,
        split: true,
        reference: 'treelistContainer',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        scrollable: 'y',
        cls:'treelist-with-nav',
        items: [{
            xtype:"nav",
            title:"导航"
        }]
    }, {
        xtype:"tabpanel",
        region: 'center',
        border: true,
        //margin: "0 5 0 0",
        layout: "fit",
        id:"ttt",
        //bodyPadding: 10
    }]
});