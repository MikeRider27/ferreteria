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
    TablaEmpleado();
    Cargar_TablaEmpleado();
    CargarCaja();
    CargarCargo();
    CargarNacionalidad();
    CargarCiudad();
});

function prepararJSONEmpleado(ban) {
    //alert("Bandera " + ban);
    JSON = {
        bandera: ban,
        id_empleado: $('#id_empleado').val(),
        nom_empleado: $('#nom_empleado').val(),
        ape_empleado: $('#ape_empleado').val(),
        cedula: $('#cedula').val(),
        direccion: $('#direccion').val(),
        telefono: $('#telefono').val(),
        id_caja: $('#id_caja').val(),
        id_cargo: $('#id_cargo').val(),
        id_nacionalidad: $('#id_nacionalidad').val(),
        id_ciudad: $('#id_ciudad').val()
    };
    enviarEmpleado();
}


function AgregarEmpleado() {
    prepararJSONEmpleado(1);
}

function ModificarEmpleado() {
    prepararJSONEmpleado(2);
}

function EliminarEmpleado() {
    prepararJSONEmpleado(3);
}

function enviarEmpleado() {
    $.ajax({
        url: "/FERRETERIA/EmpleadoMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCamposEmpleados();
            Cargar_TablaEmpleado();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function RecuperarEmpleado() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/EmpleadoMod",
        data: {
            bandera: 4,
            id_empleado: $('#id_empleado').val()
        },
        success: function (json_empleado) {

            $.each(json_empleado, function (key, value) {
                $('#id_empleado').val(value.id_empleado);
                $('#nom_empleado').val(value.nom_empleado);
                $('#ape_empleado').val(value.ape_empleado);
                $('#cedula').val(value.cedula);
                $('#direccion').val(value.direccion);
                $('#telefono').val(value.telefono);

                $('#id_caja> option[value=' + value.id_caja + ']').attr('selected', 'selected');
                $('#id_cargo> option[value=' + value.id_cargo + ']').attr('selected', 'selected');
                $('#id_nacionalidad> option[value=' + value.id_nacionalidad + ']').attr('selected', 'selected');
                $('#id_ciudad> option[value=' + value.id_ciudad + ']').attr('selected', 'selected');
                $('#nom_empleado').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}

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
                        + "<td>" + value.id_caja + "</td>" + "<td>" + value.id_cargo + "</td>"
                        + "<td>" + value.id_nacionalidad + "</td>" + "<td>" + value.id_ciudad + "</td>"
                        + "<td>" + value.empleado + "</td>" + "<td>" + value.cedula + "</td>"
                        + "<td>" + value.direccion + "</td>" + "<td>" + value.telefono + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function TablaEmpleado() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/EmpleadoMod",
        data: {
            bandera: 10
        },
        success: function (parametro) {
            $("#RowEmp").html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowEmp").append($("<tr>").append($("<td>" + value.id_empleado + "</td>"
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

function CargarCaja() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/EmpleadoMod",
        data: {
            bandera: 6
        },
        success: function (opCaja) {
            $("#id_caja").html(opCaja);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarCargo() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/EmpleadoMod",
        data: {
            bandera: 7
        },
        success: function (opCargo) {
            $("#id_cargo").html(opCargo);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarNacionalidad() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/EmpleadoMod",
        data: {
            bandera: 8
        },
        success: function (opcionesNac) {
            $("#id_nacionalidad").html(opcionesNac);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarCiudad() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/EmpleadoMod",
        data: {
            bandera: 9
        },
        success: function (opcionesCiu) {
            $("#id_ciudad").html(opcionesCiu);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarCamposEmpleados() {
    $('#id_empleado').val("");
    $('#nom_empleado').val("");
    $('#ape_empleado').val("");
    $('#cedula').val("");
    $('#cliente').val("");
    $('#cedula').val("");
    $('#id_caja').val("");
    $('#id_cargo').val("");
    $('#id_nacionalidad').val("");
    $('#id_ciudad').val("");
    $('#direccion').val("");
    $('#telefono').val("");
}