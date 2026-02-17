<?php
require_once '../modelos/modelo.php';

$user = new User();
$result = $user->getUsers();

require_once '../vistas/vista.php';
