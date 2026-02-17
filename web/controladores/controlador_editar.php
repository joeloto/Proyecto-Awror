<?php
if (isset($_GET['id']) && is_numeric($_GET['id'])) {
    require_once '../modelos/modelo.php';

    $user = new User();
    $user = $user->editar($_GET['id']); // CORRECTO: quitar $$

    $dato = $dato[0]; // para acceder directamente al primer resultado
    require_once '../vistas/vista_editar.php';
} else {
    echo "<p style='color:red;'>ID inválido</p>";
}
