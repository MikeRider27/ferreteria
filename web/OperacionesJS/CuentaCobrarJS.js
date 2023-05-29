$(document).ready(function () {
    (function ($) {
        $('#filtroCC').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsCC tr').hide();
            $('.RowsCC tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    CargarVenta();
    Cargar_TablaCC();
});

function prepararJSONcc(ban) {
    alert("Bandera " + ban);
    JSON = {
        bandera: ban,
        id_cc: $('#id_cc').val(),
        importe_total: $('#importe_total').val(),
        importe_cobrado: $('#importe_cobrado').val(),
        nro_cuota: $('#nro_cuota').val(),
        fecha: $('#fecha').val(),
        id_venta: $('#id_venta').val()
    };
    enviarCC();
}

function AgregarCC() {
    prepararJSONcc(1);
}

function ModificarCC() {
    prepararJSONcc(2);
}

function EliminarCC() {
    prepararJSONcc(3);
}

function enviarCC() {
    $.ajax({
        url: "/FERRETERIA/CuentaCobrarMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCampos();
            Cargar_TablaCC();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function RecuperarCC() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CuentaCobrarMod",
        data: {
            bandera: 6,
            id_cc: $('#id_cc').val()
        },
        success: function (json_cc) {
            alert(json_cc);

            $.each(json_cc, function (key, value) {
                $('#id_cc').val(value.id_cc);
                $('#importe_total').val(value.importe_total);
                $('#importe_cobrado').val(value.importe_cobrado);
                $('#nro_cuota').val(value.nro_cuota);
                $('#fecha').val(value.fecha);

                $('#id_venta> option[value=' + value.id_venta + ']').attr('selected', 'selected');
                $('#importe_total').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}

function Cargar_TablaCC() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CuentaCobrarMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $("#RowCC").html(parametro);
            
            $.each(parametro, function (indice, value) {
                $("#RowCC").append($("<tr>").append($("<td>" + value.id_cc + "</td>" + "<td>" + value.importe_total + "</td>"
                        + "<td>" + value.importe_cobrado + "</td>" + "<td>" + value.nro_cuota + "</td>"
                        + "<td>" + value.fecha + "</td>" + "<td>" + value.id_venta + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarVenta() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CuentaCobrarMod",
        data: {
            bandera: 5
        },
        success: function (opcionesV) {
            $("#id_venta").html(opcionesV);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarCampos() {
    $('#id_cc').val("");
    $('#importe_total').val("");
    $('#importe_cobrado').val("");
    $('#nro_cuota').val("");
    $('#fecha').val("");
    $('#id_venta').val("");
}