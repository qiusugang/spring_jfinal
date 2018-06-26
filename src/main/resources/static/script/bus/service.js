$('#serviceGrid').datagrid({
    url: '/service/page',
    pagePosition: 'bottom',
    pagination: true,
    singleSelect: true,
    fitColumns: true,
    toolbar: '#serviceGridToolBar',
    idField: "id",
    loadMsg: "正在努力为您加载数据",
    fit: true,
    rownumbers: true,
    nowrap: true,
    columns: [[
        {field: 'name', title: '服务名称', align: 'center', sortable: true},
        {field: 'code', title: '服务代码', align: 'center', sortable: true}
    ]]
});


function doSearch() {
    var param = {};
    generateCondition(param, $('#queryForm').serialize());
    $("#serviceGrid").datagrid('load', param);
}

function openUpdateServiceWin() {
    var row = $('#serviceGrid').datagrid('getSelected');
    if (row) {
        $('#updateServiceForm').form('reset');
        $('#serviceUpdateWin').window('open');
        $('#updateServiceForm').form('load', row);
    } else {
        pop('提示', "请选择要修改的数据！");
    }
}

function closeUpdateServiceWin() {
    $('#serviceUpdateWin').window('close');
}

function updateService() {
    if ($('#updateServiceForm').form('validate')) {
        $.ajax({
            type: "POST",
            url: "/service/update",
            data: $('#updateServiceForm').serialize(),
            dataType: 'json',
            success: function (obj) {
                if (obj.success) {
                    $('#serviceUpdateWin').window('close');
                    updateRowInGrid('serviceGrid', obj.data);
                    pop('温馨提示', '保存成功');
                }
                else {
                    pop('保存提示', obj.msg);
                }
            }
        });
    }
}

function openSaveServiceWin() {
    $('#saveServiceForm').form('reset');
    $('#serviceSaveWin').window('open');
}

function saveService() {
    if ($('#saveServiceForm').form('validate')) {
        $.ajax({
            type: "POST",
            url: "/service/save",
            data: $('#saveServiceForm').serialize(),
            dataType: 'json',
            success: function (obj) {
                if (obj.success) {
                    $('#serviceSaveWin').window('close');
                    insertRowToGrid('serviceGrid', obj.data);
                    pop('温馨提示', '保存成功');
                }
                else {
                    pop('保存提示', obj.msg);
                }
            }
        });
    }
}

function deleteService() {
    var row = $('#serviceGrid').datagrid('getSelected');
    if (row) {
        $.messager.confirm('提示信息', '确定删除?', function (r) {
            if (r) {
                $.ajax({
                    type: "POST",
                    url: "/service/delete?id=" + row.id,
                    success: function (obj) {
                        if (obj.success) {
                            deleteSelectedRow('serviceGrid');
                            pop('温馨提示', '删除成功');
                        }
                        else {
                            pop('删除提示', obj.msg);
                        }
                    }
                });
            }
        });
    } else {
        pop('删除提示', "请选择要删除的数据！");
    }
}

function closeSaveServiceWin() {
    $('#serviceSaveWin').window('close');
}




