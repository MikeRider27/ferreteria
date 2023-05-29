$(document).ready(function () {
    (function ($) {
        $('#filtroSucursal').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsSucursal tr').hide();
            $('.RowsSucursal tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
    SucursalHTML();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_sucursal: $('#id_sucursal').val(),
        sucursal: $('#sucursal').val(),
        telefono: $('#telefono').val(),
        direccion: $('#direccion').val()
    };
    enviarDatos();
}


function AgregarSucursal() {
    prepararJSON(1);
}

function ModificarSucursal() {
    prepararJSON(2);
}

function EliminarSucursal() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/SucursalMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarSucursales();
            Cargar_Tabla();
            SucursalHTML();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function LimpiarSucursales() {
    $('#id_sucursal').val("");
    $('#sucursal').val("");
    $('#telefono').val("");
    $('#direccion').val("");
}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/SucursalMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowSuc').html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowSuc").append($("<tr>").append($("<td>" + value.id_sucursal + "</td>" + "<td>" + value.sucursal + "</td>" +
                        "<td>" + value.telefono + "</td>" + "<td>" + value.direccion + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function SucursalHTML() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/SucursalMod",
        data: {
            bandera: 6
        },
        success: function (suc) {
            $('#RowSucursal').html(suc);

            $.each(suc, function (indice, value) {
                $("#RowSucursal").append($("<tr>").append($("<td>" + value.id_sucursal + "</td>" + "<td>" + value.sucursal + "</td>" +
                        "<td>" + value.telefono + "</td>" + "<td>" + value.direccion + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function RecuperarSucursal() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/SucursalMod",
        data: {
            bandera: 5,
            id_sucursal: $('#id_sucursal').val()
        },
        success: function (json_sucursal) {
            alert(json_sucursal);

            $.each(json_sucursal, function (key, value) {
                $('#id_sucursal').val(value.id_sucursal);
                $('#sucursal').val(value.sucursal);
                $('#telefono').val(value.telefono);
                $('#direccion').val(value.direccion);

                $('#sucursal').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}