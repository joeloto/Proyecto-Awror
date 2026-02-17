<?php
if (isset($_GET['id']) && is_numeric($_GET['id'])) {
    require_once '../modelos/modelo.php';
    $user = new User();
    $user->borrar($_GET['id']);

    header("Location: controlador.php");
    exit;
} else {
    echo "<p style='color:red;'>ID inválido</p>";
}
