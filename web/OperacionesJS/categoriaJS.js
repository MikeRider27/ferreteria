$(document).ready(function () {
        (function ($) {
        $('#filtroCategoria').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsCategoria tr').hide();
            $('.RowsCategoria tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

    function prepararJSON(ban){
        JSON={
              bandera:   ban, 
              id_categoria:   $('#id_categoria').val(),
              categoria:   $('#categoria').val()
          };
    enviarDatos();
    }
    
    
    function AgregarCategoria(){
        prepararJSON(1);
    }
    
    function ModificarCategoria(){
        prepararJSON(2);
    }
    
    function EliminarCategoria(){
        prepararJSON(3);
    }

function enviarDatos(){
    $.ajax({
        url: "/FERRETERIA/CategoriaMod",
        data: JSON,
        type: 'POST',
        success: function (respuesta) {
            LimpiarCampos();
            Cargar_Tabla(); 
        },
        error: function () {
        //alert('No se pudo obtener ultimo valor...!!!');
        }
    }); 
}

function LimpiarCamposCAT(){    
    $('#id_categoria').val("");
    $('#categoria').val("");
}

function Cargar_Tabla() {    
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CategoriaMod",
        data: {
            bandera:4
        },
        success: function (parametro) {                                               
            $('#RowCategoria').html(parametro);
            
            $.each(parametro, function (indice, value) {
                $("#RowCategoria").append($("<tr>").append($("<td>" + value.id_categoria + "</td>" + "<td>" + value.categoria + "</td>")));
            });           
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function RecuperarCategoria(){
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CategoriaMod",
        data: {
            bandera: 5,
            id_categoria: $('#id_categoria').val()
        },
        success: function (json_categoria) {
            alert(json_categoria);

            $.each(json_categoria, function (key, value) {
                $('#id_categoria').val(value.id_categoria);
                $('#categoria').val(value.categoria);
                
                $('#categoria').focus();
            });
            
        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}