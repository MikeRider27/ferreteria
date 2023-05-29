$(document).ready(function () {
    (function ($) {
        $('#filtroAP').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsAP tr').hide();
            $('.RowsAP tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));

    AChtml();
    TablaAC();
    Caja();
    CargarUser();
});

function prepararAC(ban) {
    //alert("Bandera " + ban);
    JSON = {
        bandera: ban,
        apertura: $('#apertura').val(),
        fecha1: $('#fecha1').val(),
        fecha2: $('#fecha2').val(),
        hora1: $('#hora1').val(),
        hora2: $('#hora2').val(),
        monto1: $('#monto1').val(),
        monto2: $('#monto2').val(),
        ca: $('#ca').val(),
        us: $('#us').val()
    };
    enviarAC();
}


function AgregarAC() {
    prepararAC(1);
}

function ModificarAC() {
    prepararAC(2);
}

function EliminarAC() {
    prepararAC(3);
}

function enviarAC() {
    $.ajax({
        url: "/FERRETERIA/AperturaCierreMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarAC();
            TablaAC();
            AChtml();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function RecuperarAC() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/AperturaCierreMod",
        data: {
            bandera: 7,
            apertura: $('#apertura').val()
        },
        success: function (json_ac) {
            //alert(json_ac);

            $.each(json_ac, function (key, value) {
                $('#apertura').val(value.apertura);
                $('#fecha1').val(value.fecha1);
                $('#hora1').val(value.hora1);
                $('#fecha2').val(value.fecha2);
                $('#hora2').val(value.hora2);
                $('#monto1').val(value.monto1);
                $('#monto2').val(value.monto2);
                $('#ca> option[value=' + value.ca + ']').attr('selected', 'selected');
                $('#us> option[value=' + value.us + ']').attr('selected', 'selected');
                $('#fecha').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}

function TablaAC() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/AperturaCierreMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $("#RowAC").html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowAC").append($("<tr>").append($("<td>" + value.apertura + "</td>" + "<td>" + value.ca + "</td>" + "<td>" + value.us + "</td>"
                        + "<td>" + value.fecha1 + "</td>" + "<td>" + value.hora1 + "</td>" + "<td>" + value.fecha2 + "</td>" 
                        + "<td>" + value.hora2 + "</td>" + "<td>" + value.monto1 + "</td>" + "<td>" + value.monto2 + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function AChtml() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/AperturaCierreMod",
        data: {
            bandera: 8
        },
        success: function (ac) {
            $("#acHTML").html(ac);

            $.each(ac, function (indice, value) {
                $("#acHTML").append($("<tr>").append($("<td>" + value.apertura + "</td>" + "<td>" + value.ca + "</td>" + "<td>" + value.us + "</td>"
                        + "<td>" + value.fecha1 + "</td>" + "<td>" + value.hora1 + "</td>" + "<td>" + value.fecha2 + "</td>" 
                        + "<td>" + value.hora2 + "</td>" + "<td>" + value.monto1 + "</td>" + "<td>" + value.monto2 + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function Caja() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/AperturaCierreMod",
        data: {
            bandera: 5
        },
        success: function (opca) {
            $("#ca").html(opca);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarUser() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/AperturaCierreMod",
        data: {
            bandera: 6
        },
        success: function (user) {
            $("#us").html(user);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarAC() {
    $('#apertura').val("");
    $('#us').val("");
    $('#ca').val("");
    $('#monto1').val("");
    $('#monto2').val("");
    $('#hora1').val("");
    $('#hora2').val("");
    $('#fecha1').val("");
    $('#fecha2').val("");
}