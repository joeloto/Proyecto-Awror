<?php
if (
    !empty($_POST['nombre']) &&
    !empty($_POST['artista']) &&
    !empty($_POST['ano']) &&
    !empty($_POST['genero'])
) {
    require_once '../modelos/modelo.php';
    $user = new User();

    // Comprobar si se ha subido imagen
    if (isset($_FILES['imagen']) && $_FILES['imagen']['error'] === UPLOAD_ERR_OK) {
        $directorio = "../img/";
        $nombreArchivo = basename($_FILES['imagen']['name']);
        $ruta = $directorio . $nombreArchivo;

        if (move_uploaded_file($_FILES['imagen']['tmp_name'], $ruta)) {
            $ruta_final = "img/" . $nombreArchivo;
        } else {
            $ruta_final = $_POST['antigua']; // si falla subir imagen, mantenemos la antigua
        }
    } else {
        $ruta_final = $_POST['antigua']; // si no se selecciona nueva imagen
    }

    $result = $user->actualizar(
        $_POST['id'],
        $_POST['real_name'],
        $_POST['real_surname'],
        $_POST['email'],
        $_POST['password'],
        $_POST['user_name'],
        $ruta_final
    );

    if ($result) {
        echo "<p style='color:green;'>Álbum actualizado correctamente</p>";
    } else {
        echo "<p style='color:red;'>Error al actualizar el álbum</p>";
    }
} else {
    echo "<p style='color:red;'>Faltan datos obligatorios</p>";
}
