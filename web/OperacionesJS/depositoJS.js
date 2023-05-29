$(document).ready(function () {
    (function ($) {
        $('#filtroDeposito').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsDeposito tr').hide();
            $('.RowsDeposito tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    //TablaDepositoHTML();
    TablaDeposito();
    CargarSucursal();
});

function mostrarModalSucursal(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_sucursal').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_sucursal').style.display = 'none';
    }
}

function recorrerTblBusSucursal() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_sucursales');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_sucursal");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_sucursal').val(trOb[x].id);
            mostrarModalSucursal(false);
        }
    }
}

function prepararDepositos(ban) {
   // alert("Bandera " + ban);
    JSON = {
        bandera: ban,
        id_deposito: $('#id_deposito').val(),
        id_sucursal: $('#id_sucursal').val(),
        sucursal: $('#sucursal').val(),
        deposito: $('#deposito').val()
    };
    enviarDeposito();
}

function AgregarDeposito() {
    prepararDepositos(1);
}

function ModificarDeposito() {
    prepararDepositos(2);
}

function EliminarDeposito() {
    prepararDepositos(3);
}

function enviarDeposito() {
    $.ajax({
        url: "/FERRETERIA/DepositoMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarDeposito();
            TablaDeposito();
            TablaDepositoHTML();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function TablaDeposito() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/DepositoMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $("#Deposito").html(parametro);
            $.each(parametro, function (indice, value) {
                $("#Deposito").append($("<tr>").append($("<td>" + value.id_deposito + "</td>"
                        + "<td>" + value.deposito + "</td>" + "<td>" + value.id_sucursal + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarSucursal() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/DepositoMod",
        data: {
            bandera: 5
        },
        success: function (Suc) {
            $('#id_sucursal').html(Suc);

        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarDeposito() {
    $('#id_sucursal').val("");
    ('#sucursal').val("");
    $('#deposito').val("");
    $('#id_deposito').val("");
}

function RecuperarDeposito() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/DepositoMod",
        data: {
            bandera: 6,
            id_deposito: $('#id_deposito').val()
        },
        success: function (json_Dep) {
            $.each(json_Dep, function (key, value) {
                $('#id_deposito').val(value.id_deposito);
                $('#id_sucursal').val(value.id_sucursal);
                $('#sucursal').val(value.sucursal);
                $('#deposito').val(value.deposito);
                $('#id_deposito').focus();
            });
        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}

//function TablaDepositoHTML() {
//    $.ajax({
//        type: 'POST',
//        url: "/FERRETERIA/DepositoMod",
//        data: {
//            bandera: 7
//        },
//        success: function (OPCdep) {
//            $("#DepositoHTML").html(OPCdep);
//            $.each(OPCdep, function (indice, value) {
//                $("#DepositoHTML").append($("<tr>").append($("<td>" + value.id_deposito + "</td>"
//                        + "<td>" + value.deposito + "</td>" + "<td>" + value.id_sucursal + "</td>")));
//            });
//        },
//        error: function (e) {
//            //alert('Error al Cargar Tabla.: '+e);
//        }
//    });
//}