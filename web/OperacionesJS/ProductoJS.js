$(document).ready(function () {
    (function ($) {
        $('#filtroProducto').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsProducto tr').hide();
            $('.RowsProducto tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    ProductoHTML();
    Cargar_Producto();
    CargarMarca();
    CargarCategoria();
    CargaraIVA();
});

function prepararJSONProducto(ban) {
    //alert("Bandera " + ban);
    JSON = {
        bandera: ban,
        id_producto: $('#id_producto').val(),
        producto: $('#producto').val(),
        precio_costo: $('#precio_costo').val(),
        precio_venta: $('#precio_venta').val(),
        iva: $('#iva').val(),
        id_marca: $('#id_marca').val(),
        id_categoria: $('#id_categoria').val()

    };
    enviarProducto();
}

function AgregarProducto() {
    prepararJSONProducto(1);
}

function ModificarProducto() {
    prepararJSONProducto(2);
}

function EliminarProducto() {
    prepararJSONProducto(3);
}

function enviarProducto() {
    $.ajax({
        url: "/FERRETERIA/ProductoMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCamposProducto();
            Cargar_Producto();
            ProductoHTML();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function Cargar_Producto() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ProductoMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $("#RowProd").html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowProd").append($("<tr>").append($("<td>" + value.id_producto + "</td>"
                        + "<td>" + value.producto + "</td>" + "<td>" + value.precio_costo + "</td>"
                        + "<td>" + value.precio_venta + "</td>" + "<td>" + value.iva + "</td>"
                        + "<td>" + value.id_marca + "</td>" + "<td>" + value.id_categoria + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function ProductoHTML() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ProductoMod",
        data: {
            bandera: 8
        },
        success: function (prohtml) {
            $("#RowProducto").html(prohtml);

            $.each(prohtml, function (indice, value) {
                $("#RowProducto").append($("<tr>").append($("<td>" + value.id_producto + "</td>" + "<td>" + value.producto + "</td>"
                        + "<td>" + value.id_marca + "</td>" + "<td>" + value.id_categoria + "</td>"+ "<td>" + value.precio_venta + "</td>"
                        + "<td>" + value.iva + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarMarca() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ProductoMod",
        data: {
            bandera: 6
        },
        success: function (opmarca) {
            $("#id_marca").html(opmarca);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargaraIVA() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ProductoMod",
        data: {
            bandera: 9
        },
        success: function (opiva) {
            $("#iva").html(opiva);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarCategoria() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ProductoMod",
        data: {
            bandera: 7
        },
        success: function (opCat) {
            $('#id_categoria').html(opCat);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarCamposProducto() {
    $('#id_producto').val("");
    $('#producto').val("");

    $('#precio_costo').val("");
    $('#precio_venta').val("");
    $('#iva').val("");
    $('#id_marca').val("");
    $('#id_categoria').val("");
}

function RecuperarProducto() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ProductoMod",
        data: {
            bandera: 5,
            id_producto: $('#id_producto').val()
        },
        success: function (json_Producto) {
            $.each(json_Producto, function (key, value) {
                $('#id_producto').val(value.id_producto);
                $('#producto').val(value.producto);

                $('#precio_costo').val(value.precio_costo);
                $('#precio_venta').val(value.precio_venta);
                $('#id_marca> option[value=' + value.id_marca + ']').attr('selected', 'selected');
                $('#id_categoria> option[value=' + value.id_categoria + ']').attr('selected', 'selected');
                $('#iva> option[value=' + value.iva + ']').attr('selected', 'selected');
                $('#producto').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}