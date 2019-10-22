// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
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
                        "desc"
                    ]
                ]
            })
        }
    );
});

// function filter() {
//     $.ajax({
//         type: "POST",
//         url: context.ajaxUrl + "filter",
//         data: $('#filterForm').serialize(),
//     }).done(function () {
//         // $("#editRow").modal("hide");
//         updateTable();
//         successNoty("Saved");
//     });
// }

function filter() {
    $.get( { url : context.ajaxUrl + "filter",
            data: $('#filterForm').serialize(),
        success : function (data) {
            context.datatableApi.clear().rows.add(data).draw();
        }});
}


// function updateTable() {
//     $.get(context.ajaxUrl, function (data) {
//         context.datatableApi.clear().rows.add(data).draw();
//     });
// }