$(document).ready(function () {
    (function ($) {
        $('#filtroCliente').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsCliente tr').hide();
            $('.RowsCliente tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_TablaCliente();
    CargarNacionalidad();
    CargarCiudad();
});

function prepararJSONCliente(ban) {
    //alert("Bandera " + ban);
    JSON = {
        bandera: ban,
        id_cliente: $('#id_cliente').val(),
        nom_cliente: $('#nom_cliente').val(),
        ape_cliente: $('#ape_cliente').val(),
        cedula: $('#cedula').val(),
        ruc: $('#ruc').val(),
        direccion: $('#direccion').val(),
        telefono: $('#telefono').val(),
        id_nacionalidad: $('#id_nacionalidad').val(),
        nacionalidad: $('#nacionalidad').val(),
        id_ciudad: $('#id_ciudad').val(),
        ciudad: $('#ciudad').val()
    };
    enviarCliente();
}


function AgregarCliente() {
    prepararJSONCliente(1);
}

function ModificarCliente() {
    prepararJSONCliente(2);
}

function EliminarCliente() {
    prepararJSONCliente(3);
}

function enviarCliente() {
    $.ajax({
        url: "/FERRETERIA/ClienteMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCamposClientes();
            Cargar_TablaCliente();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function RecuperarCliente() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ClienteMod",
        data: {
            bandera: 7,
            id_cliente: $('#id_cliente').val()
        },
        success: function (json_cliente) {
            //alert(json_cliente);

            $.each(json_cliente, function (key, value) {
                $('#id_cliente').val(value.id_cliente);
                $('#nom_cliente').val(value.nom_cliente);
                $('#ape_cliente').val(value.ape_cliente);
                $('#cedula').val(value.cedula);
                $('#ruc').val(value.ruc);
                $('#direccion').val(value.direccion);
                $('#telefono').val(value.telefono);

                $('#id_nacionalidad> option[value=' + value.id_nacionalidad + ']').attr('selected', 'selected');
                $('#id_ciudad> option[value=' + value.id_ciudad + ']').attr('selected', 'selected');
                $('#nom_cliente').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}



function Cargar_TablaCliente() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ClienteMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $("#RowCliente").html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowCliente").append($("<tr>").append($("<td>" + value.id_cliente + "</td>"+ "<td>" + value.nacionalidad + "</td>" 
                        + "<td>" + value.ciudad + "</td>" + "<td>" + value.cliente + "</td>" + "<td>" + value.cedula + "</td>" + "<td>" 
                        + value.ruc + "</td>" + "<td>" + value.direccion + "</td>" + "<td>" + value.telefono + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarNacionalidad() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/ClienteMod",
        data: {
            bandera: 5
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
        url: "/FERRETERIA/ClienteMod",
        data: {
            bandera: 6
        },
        success: function (opcionesCiu) {
            $("#id_ciudad").html(opcionesCiu);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarCamposClientes() {
    $('#id_nacionalidad').val("");
    $('#id_ciudad').val("");
    $('#id_cliente').val("");
    $('#nom_cliente').val("");
    $('#ape_cliente').val("");
    $('#cedula').val("");
    $('#ruc').val("");
    $('#direccion').val("");
    $('#telefono').val("");
}