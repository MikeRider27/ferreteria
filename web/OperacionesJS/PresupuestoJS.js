$(document).ready(function () {
    //alert('Cab-Deta');
    
    
});
var acu = 0;
var tindex = 0;
var factura;
var pedidoJSON;
var jsonCabDet;

function mostrarUsuario(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_usuario').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_usuario').style.display = 'none';
    }
}

function recorrerUsuario() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_usuarios');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_usuario");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_usuario').val(trOb[x].id); 
            mostrarUsuario(false);
        }
    }
}

function mostrarProducto(mostrar) {
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
            mostrarProducto(false);
        }
    }
}

function prepararJsonPresupuesto() {
    var listaProductos = [];
    $('#tabladetalleproductos tr').each(function () {
        //push => Agrega un nuevo elemento el Array listaProductos
        listaProductos.push({
            id_producto: $(this).find("td").eq(0).html(),
            descripcion: $(this).find("td").eq(1).html(),
            cantidad: $(this).find("td").eq(2).html(),
            precio_venta: $(this).find("td").eq(3).html()
        });
    });
    jsonPresupuesto = {
        "bandera": 1,
        "id_presupuesto": $('#id_presupuesto').val().length <= 0 ? "0" : $('#id_presupuesto').val(),
        "id_usuario": $('#id_usuario').val().length <= 0 ? "0" : $('#id_usuario').val(),
        "fecha": $('#fecha').val().length <= 0 ? "0" : $('#fecha').val(),
        "observacion": $('#observacion').val().length <= 0 ? "0" : $('#observacion').val(),
        "detalle": listaProductos.length <= 0 ? "0" : listaProductos
    };
    jsonCabDet = JSON.stringify(jsonPresupuesto);
}

function AgregarPresupuesto() {
    prepararJsonPresupuesto();
    $.ajax({
        url: "/FERRETERIA/GenerarPresupuestoMod",
        data: jsonCabDet,
        type: 'POST',
        success: function () {
            limpiarPresupuesto();
            limpiarDetaProductos();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function agregarFilaPresupuesto() {
    var cod = $('#id_producto').val();
    var des = $('#descrip_producto').val();
    var cant = $('#cantidad').val();
    var precio = $('#precio_producto').val();
    var calculo = cant * precio;
    acu = acu + calculo;
    $('#montototalpresupuesto').val(acu);
    tindex++;
    $('#tabladetalleproductos').append("<tr id=\'prod" + tindex + "\'>\n\
            <td style=' width: 1%;'>" + cod + "</td>\n\
            <td style=' width: 60%;'>" + des + "</td>\n\
            <td style=' width: 2%;'>" + cant + "</td>\n\
            <td style=' width: 25%;'>" + precio + "</td>\n\
            <td style=' width: 30%;'>" + calculo + "</td>\n\
            <td style=' width: 1%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='Recursos/img/update.png'/></td>\n\
            <td style=' width: 1%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='Recursos/img/delete.png'/></td>\n\
      </tr>");
}

function RecuperarPresupuesto() {
    $.ajax({
        url: "/FERRETERIA/GenerarPresupuestoMod",
        data: {"bandera": 2,
            "id_presupuesto": $('#id_presupuesto').val()
        },
        type: 'POST',
        success: function (jsonPres) {
            //alert(jsonPres);         
            $.each(jsonPres, function (i, value) {
                //Datos de cabecera
                $('#id_usuario').val(value.id_usuario);
                $('#usuario').val(value.usuario);
                $('#fecha').val(value.fecha);
                $('#observacion').val(value.observacion);
                //Datos del detalle
                $.each(value.detalle, function (ind, item) {
                    var cod = item.id_producto;
                    var des = item.descripcion;
                    var cant = item.cantidad;
                    var precio = item.precio_venta;
                    var calculo = cant * precio;
                    acu = acu + calculo;
                    $('#montototalpresupuesto').val(acu);
                    tindex++;
                    $('#tabladetalleproductos').append("<tr id=\'prod" + tindex + "\'>\n\
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
            alert('No se encuentra el codigo');
        }
    });
}

function AnularPresupuesto() {
    if (confirm("Confirmar la Anulación del Presupuesto")) {
        $.ajax({
            url: "/FERRETERIA/GenerarPresupuestoMod",
            data: {"bandera": 3,
                "id_presupuesto": $('#id_presupuesto').val()
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
    $('#precio_producto').val(null);
    $('#id_producto').focus();
    $('#tabladetalleproductos').val("");
}

function limpiarPresupuesto() {
    $('#id_producto').val(null);
    $('#descrip_producto').val(null);
    $('#cantidad').val(null);
    $('#precio_producto').val(null);
    
    $('#id_presupuesto').val(null);
    $('#id_usuario').val(null);
    $('#usuario').val(null);
    $('#fecha').val(null);
    $('#observacion').val(null);
    $('#tabladetalleproductos').val("");
    $('#id_presupuesto').focus();    
}

function MostrarAyuda() {
    $.ajax({
        url: "/FERRETERIA/MostrarAyuda",
        type: 'POST'
    });
}