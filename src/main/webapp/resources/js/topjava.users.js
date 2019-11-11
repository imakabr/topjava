// $(document).ready(function () {

$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
});

function checkbox(chkbox, id) {
    const enabled = chkbox.is(":checked");
//  https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: context.ajaxUrl + id,
        type: "POST",
        data: "isEnabled=" + enabled
    }).done(function () {
        successNoty(enabled ? "Enabled" : "Disabled");
        chkbox.closest("tr").attr("active", enabled);
    }).fail(function () {
        $(chkbox).prop("checked", !enabled);
    });
}
