$(document).ready(function () {
    (function ($) {
        $('#filtroMarca').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsMarca tr').hide();
            $('.RowsMarca tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_marca: $('#id_marca').val(),
        marca: $('#marca').val()
    };
    enviarDatos();
}

function AgregarMarca() {
    prepararJSON(1);
}

function ModificarMarca() {
    prepararJSON(2);
}

function EliminarMarca() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/MarcaMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarMarcas();
            Cargar_Tabla();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function LimpiarMarcas() {
    $('#id_marca').val("");
    $('#marca').val("");

}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/MarcaMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowMarcas').html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowMarcas").append($("<tr>").append($("<td>" + value.id_marca + "</td>" + "<td>" + value.marca + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function RecuperarMarca() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/MarcaMod",
        data: {
            bandera: 5,
            id_marca: $('#id_marca').val()
        },
        success: function (json_marca) {
            //alert(json_marca);

            $.each(json_marca, function (key, value) {
                $('#id_marca').val(value.id_marca);
                $('#marca').val(value.marca);

                $('#marca').focus();
            });
        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}