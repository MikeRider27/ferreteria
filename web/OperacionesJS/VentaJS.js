$(document).ready(function () {
    Documento();
    Condicion();
});

var acu = 0;
var tindex = 0;
var factura;
var pedidoJSON;
var jsonCabDet;

function mostrarAC(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_ac').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_ac').style.display = 'none';
    }
}

function recorrerAC() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_ac');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_ac");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_ac').val(trOb[x].id);
            mostrarAC(false);
        }
    }
}

function mostrarUser(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_usuario').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_usuario').style.display = 'none';
    }
}

function recorrerUser() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_usuarios');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_usuario");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_usuario').val(trOb[x].id);
            mostrarUser(false);
        }
    }
}

function mostrarProd(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_producto').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_producto').style.display = 'none';
    }
}

function recorrerProd() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_productos');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_producto");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_producto').val(trOb[x].id);
            mostrarProd(false);
        }
    }
}


function crearJsonCabDetMaestro() {
    //declaramos una variable Array
    var listaArticulos = [];
    $("#tabladetalleproducto tr").each(function () {
        //push => Agrega un nuevo elemento al Array [listaArticulos]
        listaArticulos.push({
            id_producto: $(this).find("td").eq(0).html(),
            cantidad: $(this).find("td").eq(2).html(),
            precio_venta: $(this).find("td").eq(3).html()
        });
    });
    pedidoJSON = {
        "bandera": 1,
        "id_venta": $('#id_venta').val().length <= 0 ? "0" : $('#id_venta').val(),
        "id_ac": $('#id_ac').val().length <= 0 ? "0" : $('#id_ac').val(),
        "fecha": $('#fecha').val().length <= 0 ? "0" : $('#fecha').val(),
        "id_documento": $('#id_documento').val().length <= 0 ? "0" : $('#id_documento').val(),
        "id_cv": $('#id_cv').val().length <= 0 ? "0" : $('#id_cv').val(),
        "id_usuario": $('#id_usuario').val().length <= 0 ? "0" : $('#id_usuario').val(),
        "obs": $('#obs').val().length <= 0 ? "0" : $('#obs').val(),
        "detalle": listaArticulos.length <= 0 ? "0" : listaArticulos
    };
    // El método JSON.stringify() convierte un valor dado en javascript a una cadena  JSON, 
    jsonCabDet = JSON.stringify(pedidoJSON);
}

function AgregarPedido() {
    crearJsonCabDetMaestro();
    $.ajax({
        url: "/FERRETERIA/VentaMod",
        data: jsonCabDet,
        type: 'POST',
        success: function () {

        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function agregarFila() {
    var cod = $('#id_producto').val();
    var des = $('#producto').val();
    var cant = $('#cantidad').val();
    var precio = $('#precio').val();
    var calculo = cant * precio;
    acu = acu + calculo;
    $('#montototalpedido').val(acu);
    tindex++;
    $('#tabladetalleproducto').append("<tr id=\'prod" + tindex + "\'>\n\
            <td style=' width: 5%;'>" + cod + "</td>\n\
            <td style=' width: 60%;'>" + des + "</td>\n\
            <td style=' width: 5%;'>" + cant + "</td>\n\
            <td style=' width: 5%;'>" + precio + "</td>\n\
            <td style=' width: 25%;'>" + calculo + "</td>\n\
            <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='Recursos/img/update.png'/></td>\n\
            <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='Recursos/img/delete.png'/></td>\n\
      </tr>");
}

function RecuperarPedido() {
    $.ajax({
        url: "/FERRETERIA/VentaMod",
        data: {"bandera": 2,
            "id_venta": $('#id_venta').val()
        },
        type: 'POST',
        success: function (jsonPres) {
            //alert(jsonPres);         
            $.each(jsonPres, function (i, value) {
                //Datos de cabecera
                $('#id_ac').val(value.id_ac);
                $('#fecha').val(value.fecha);
                $('#id_documento').val(value.id_documento);
                $('#documento').val(value.documento);
                $('#id_cv').val(value.id_cv);
                $('#condicion').val(value.condicion);
                $('#id_usuario').val(value.id_usuario);
                $('#usuario').val(value.usuario);
                $('#obs').val(value.obs);
                //Datos del detalle
                $.each(value.detalle, function (ind, item) {
                    var cod = item.id_producto;
                    var des = item.producto;
                    var cant = item.cantidad;
                    var precio = item.precio_venta;
                    var calculo = cant * precio;
                    acu = acu + calculo;
                    $('#montototalpedido').val(acu);
                    tindex++;
                    $('#tabladetalleproducto').append("<tr id=\'prod" + tindex + "\'>\n\
                        <td style=' width: 5%;'>" + cod + "</td>\n\
                        <td style=' width: 60%;'>" + des + "</td>\n\
                        <td style=' width: 5%;'>" + cant + "</td>\n\
                        <td style=' width: 25%;'>" + precio + "</td>\n\
                        <td style=' width: 25%;'>" + calculo + "</td>\n\
                        <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='Recursos/img/update.png'/></td>\n\
                        <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='Recursos/img/delete.png'/></td>\n\
                  </tr>");
                });
            });
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function AnularPedido() {
    if (confirm("Confirmar la Anulación del Presupuesto")) {
        $.ajax({
            url: "/FERRETERIA/VentaMod",
            data: {"bandera": 3,
                "id_venta": $('#id_venta').val()
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

function limpiarCamposDatalle() {
    $('#id_producto').val(null);
    $('#producto').val(null);
    $('#cantidad').val(null);
    $('#precio').val(null);
    $('#id_producto').focus();
}

function limpiarPedido() {
    $('#id_producto').val(null);
    $('#producto').val(null);
    $('#cantidad').val(null);
    $('#precio').val(null);

    $('#id_venta').val(null);
    $('#id_ac').val(null);
    $('#id_cv').val(null);
    $('#condicion').val(null);
    $('#id_documento').val(null);
    $('#documento').val(null);
    $('#id_usuario').val(null);
    $('#usuario').val(null);
    $('#fecha').val(null);
    $('#obs').val(null);
    $('#id_venta').focus();
}

function Documento() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/VentaMod",
        data: {
            bandera: 3
        },
        success: function (doc) {
            $('#id_documento').html(doc);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function Condicion() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/VentaMod",
        data: {
            bandera: 4
        },
        success: function (cond) {
            $('#id_cv').html(cond);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}