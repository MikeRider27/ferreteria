$(document).ready(function () {
    (function ($) {
        $('#filtroEmpleado').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsEmpleado tr').hide();
            $('.RowsEmpleado tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_TablaEmpleado();
});

function Cargar_TablaEmpleado() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/EmpleadoMod",
        data: {
            bandera: 5
        },
        success: function (parametro) {
            $("#RowEmpleado").html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowEmpleado").append($("<tr>").append($("<td>" + value.id_empleado + "</td>"
                        + "<td>" + value.empleado + "</td>" + "<td>" + value.cedula + "</td>"
                        + "<td>" + value.direccion + "</td>" + "<td>" + value.telefono + "</td>"
                        + "<td>" + value.id_caja + "</td>" + "<td>" + value.id_cargo + "</td>"
                        + "<td>" + value.id_nacionalidad + "</td>" + "<td>" + value.id_ciudad + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}