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
            }), updateTable: filter
        }
    );
});

function filter() {
    $.get( { url : context.ajaxUrl + "filter",
            data: $('#filterForm').serialize(),
        success : updateTableByData});
}

function clearFilter() {
    $("#filterForm")[0].reset();
    $.get("ajax/meals/", updateTableByData);
}
