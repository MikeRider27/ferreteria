$(document).ready(function () {

});

var acu = 0;
var tindex = 0;
var factura;
var pedidoJSON;
var jsonCabDet;

//function mostrarModalDeposito(mostrar) {
//    if (mostrar) {
//        document.getElementById('modal_buscador_deposito').style.display = 'block';
//    } else {
//        document.getElementById('modal_buscador_deposito').style.display = 'none';
//    }
//}
//
//function recorrerTblBusDeposito() {
//    var tableOb, trOb;
//    tableOb = document.getElementById('datos_depositos');
//    if (!tableOb)
//        return;
//    trOb = tableOb.getElementsByClassName("opcion_seleccion_deposito");
//    for (var x = 0; x < trOb.length; x++) {
//        if (trOb[x].checked) {
//            $('#id_deposito').val(trOb[x].id);
//            mostrarModalDeposito(false);
//        }
//    }
//}

function mostrarSucursal(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_sucursal').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_sucursal').style.display = 'none';
    }
}

function recorrerSucursal() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_sucursales');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_sucursal");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_sucursal').val(trOb[x].id);
            mostrarSucursal(false);
        }
    }
}

function Producto(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_producto').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_producto').style.display = 'none';
    }
}

function recorrerProducto() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_productos');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_producto");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_producto').val(trOb[x].id);
            Producto(false);
        }
    }
}

function prepararJsonExistencia() {
    var listaProd = [];
    $('#tabladetalleprod tr').each(function () {
        //push => Agrega un nuevo elemento el Array listaProductos
        listaProd.push({
            id_producto: $(this).find("td").eq(0).html(),
            cantidad: $(this).find("td").eq(2).html()

        });
    });
    jsonDeposito = {
        "bandera": 1,
        "id_deposito": $('#id_deposito').val().length <= 0 ? "0" : $('#id_deposito').val(),
        "deposito": $('#deposito').val().length <= 0 ? "0" : $('#deposito').val(),
        "id_sucursal": $('#id_sucursal').val().length <= 0 ? "0" : $('#id_sucursal').val(),
        "detalle": listaProd.length <= 0 ? "0" : listaProd
    };
    jsonCabDet = JSON.stringify(jsonDeposito);
}

function AgregarExistencia() {
    prepararJsonExistencia();
    $.ajax({
        url: "/FERRETERIA/ExistenciaMod",
        data: jsonCabDet,
        type: 'POST',
        success: function () {
            limpiarDetaProductos();
            limpiarExistencia();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function agregarFilaExistencia() {
    var cod = $('#id_producto').val();
    var des = $('#descrip_producto').val();
    var cant = $('#cantidad').val();
    var calculo = cant * 1;
    acu = acu + calculo;
    $('#totalexistencia').val(acu);
    tindex++;
    $('#tabladetalleprod').append("<tr id=\'prod" + tindex + "\'>\n\
            <td style=' width: 5%;'>" + cod + "</td>\n\
            <td style=' width: 640%;'>" + des + "</td>\n\
            <td style=' width: 5%;'>" + cant + "</td>\n\
            <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + tindex + ")\" src='Recursos/img/update.png'/></td>\n\
            <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + tindex + ")\" src='Recursos/img/delete.png'/></td>\n\
      </tr>");
}

function RecuperarExistencia() {
    $.ajax({
        url: "/FERRETERIA/ExistenciaMod",
        data: {"bandera": 2,
            "id_deposito": $('#id_deposito').val()
        },
        type: 'POST',
        success: function (jsonPres) {
            //alert(jsonPres);         
            $.each(jsonPres, function (i, value) {
                //Datos de cabecera
                $('#id_sucursal').val(value.id_sucursal);
                $('#sucursal').val(value.sucursal);
                $('#deposito').val(value.deposito);
                //Datos del detalle
                $.each(value.detalle, function (ind, item) {
                    var cod = item.id_producto;
                    var des = item.producto;
                    var cant = item.cantidad;
                    var calculo = cant + cant;
                    $('#totalexistencia').val(calculo);
                    tindex++;
                    $('#tabladetalleprod').append("<tr id=\'prod" + tindex + "\'>\n\
                        <td style=' width: 5%;'>" + cod + "</td>\n\
                        <td style=' width: 60%;'>" + des + "</td>\n\
                        <td style=' width: 5%;'>" + cant + "</td>\n\
                        <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + tindex + ")\" src='Recursos/img/update.png'/></td>\n\
                        <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + tindex + ")\" src='Recursos/img/delete.png'/></td>\n\
                  </tr>");
                });
            });
        },
        error: function () {
            alert('No se encuentra el codigo');
        }
    });
}

function AnularExistencia() {
    if (confirm("Confirmar la Anulación de la Existencia")) {
        $.ajax({
            url: "/FERRETERIA/ExistenciaMod",
            data: {"bandera": 3,
                "id_deposito": $('#id_deposito').val()
            },
            type: 'POST',
            success: function () {
                alert('Anulación Exitosa');
            },
            error: function () {
                alert('Anulación no fue Exitosa');
            }
        });
    }
}

function limpiarDetaProductos() {
    $('#id_producto').val(null);
    $('#descrip_producto').val(null);
    $('#cantidad').val(null);
    $('#id_producto').focus();
}

function limpiarExistencia() {
    $('#id_producto').val(null);
    $('#descrip_producto').val(null);
    $('#cantidad').val(null);
    $('#precio_producto').val(null);

    $('#id_deposito').val(null);
    $('#id_sucursal').val(null);
    $('#sucursal').val(null);
    $('#deposito').val(null);
    $('#tabladetalleprod').val(null);
    $('#id_deposito').focus();
}