// $(document).ready(function () {
const userAjaxUrl = "ajax/admin/users/";

$(function () {
    makeEditable({
            ajaxUrl: userAjaxUrl,
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
            }),
        updateTable: function () {
            $.get(userAjaxUrl, updateTableByData);
        }
        }
    );
});

// function checkbox(id, isEnabled) {
//     const Enabled = isEnabled;
//     // if (confirm('Are you sure?')) {
//     // }
//     $.ajax({
//         url: context.ajaxUrl + id,
//         type: 'post',
//         data: {isEnabled : !isEnabled},
//     }).done(function (isEnabled) {
//         // updateTable();
//         // successNoty("Updated");
//         successNoty(!Enabled ? "Enabled" : "Disabled");
//         chkbox.closest("tr").attr("active", !isEnabled);
//     }).fail(function () {
//         $(chkbox).prop("checked", isEnabled);
//     });
//
// };

function checkbox(chkbox, id) {
    const enabled = chkbox.is(":checked");
//  https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: ajaxUrl + id,
        type: "POST",
        data: "enabled=" + enabled
    }).done(function () {
        successNoty(enabled ? "Enabled" : "Disabled");
        chkbox.closest("tr").attr("active", enabled);
    }).fail(function () {
        $(chkbox).prop("checked", !enabled);
    });
}
