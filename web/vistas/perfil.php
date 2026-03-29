<?php
session_start();

if (isset($_POST['logout'])) {
    session_destroy();
    header("Location: ../iniciosesion.php");
    exit();
}

if (!isset($_SESSION['id'])) {
    header("Location: ../iniciosesion.php");
    exit();
}

$mensaje = '';
$id = $_SESSION['id'];

$ch = curl_init("http://localhost:8080/apiawror/rest/users/$id");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$response = curl_exec($ch);

if ($response === false) {
    curl_close($ch);
    session_destroy();
    header("Location: ../iniciosesion.php");
    exit();
}

$status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if ($status == 200) {
    $usuario = json_decode($response, true);
} else {
    session_destroy();
    header("Location: ../iniciosesion.php");
    exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['guardar'])) {

    $update_data = [
        'user_name'     => trim($_POST['user_name']),
        'real_name'     => trim($_POST['real_name']),
        'real_surname'  => trim($_POST['real_surname']),
        'email'         => trim($_POST['email'])
    ];

    if (!empty($_POST['password'])) {
        $update_data['password'] = $_POST['password'];
    }

    $ch = curl_init("http://localhost:8080/apiawror/rest/users/$id");
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($update_data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);

    $response = curl_exec($ch);
    $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($status == 200) {
        $mensaje = "Datos actualizados correctamente.";

        $_SESSION['user_name'] = $update_data['user_name'];
        $_SESSION['real_name'] = $update_data['real_name'];
        $_SESSION['real_surname'] = $update_data['real_surname'];
        $_SESSION['email'] = $update_data['email'];

        header("Location: perfil.php");
        exit();
    } else {
        $mensaje = "Error al actualizar.";
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['borrar'])) {

    $ch = curl_init("http://localhost:8080/apiawror/rest/users/$id");
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    curl_exec($ch);
    $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($status == 200) {
        session_destroy();
        header("Location: ../iniciosesion.php");
        exit();
    } else {
        $mensaje = "Error al borrar cuenta.";
    }
}
?>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil - AWROR</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, sans-serif;
        }

        body {
            background-color: #18191a;
            color: #e4e6eb;
        }

        .pagina {
            display: grid;
            grid-template-columns: 1fr 320px;
            grid-template-rows: 70px 1fr;
            height: 100vh;
            background-image: url('../fondoawror2.jpg');
        }

        header {
            grid-column: 1/3;
            background-color: #242526;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
        }

        .logo img {
            width: 50px;
            height: 50px;
            border-radius: 8px;
            transition: 0.2s;
        }

        .logo img:hover {
            opacity: 0.8;
            transform: scale(1.05);
        }

        .tituloapp a {
            color: #f7c775;
            font-weight: bold;
            font-size: 25px;
            text-decoration: none;
        }

        .tituloapp a:hover {
            color: white;
        }

        .main {
            padding: 40px;
            display: flex;
            justify-content: center;
        }

        .formulario {
            background-color: #242526;
            width: 600px;
            border-radius: 12px;
            padding: 20px;
            border: 1px solid #3a3b3c;
        }

        .datos {
            margin: 15px 0;
        }

        input {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: none;
        }

        .acciones {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .boton-guardar {
            background-color: #ea9d55;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
            margin-top: 10px;
        }

        .boton-guardar:hover {
            background-color: #f7c775;
        }

        .boton-borrar {
            background-color: #b02a37;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            margin-top: 15px;
        }

        .boton-borrar:hover {
            background-color: #772222;
        }

        .mensaje {
            color: green;
            font-weight: bold;
            margin-top: 15px;
            text-align: center;
        }

        .opciones {
            background-color: #242526;
            border-left: 1px solid #3a3b3c;
            padding: 30px 20px;
        }

        .perfil {
            display: flex;
            align-items: center;
            margin-bottom: 30px;
        }

        .perfil .fotoperfil {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background-color: #3a3b3c;
        }

        .menu {
            list-style: none;
            padding: 0;
        }

        .menu a {
            display: block;
            padding: 12px 10px;
            border-radius: 8px;
            text-decoration: none;
            color: #e4e6eb;
            transition: 0.2s;
        }

        .menu a:hover {
            background-color: #3a3b3c;
            color: #f7c775;
        }
    </style>
</head>

<body>

    <div class="pagina">
        <header>
            <div class="logo"><a href="./principal.php"><img src="../awrorlogo.png"></a></div>
            <div class="tituloapp"><a href="./principal.php">AWROR</a></div>
        </header>

        <main class="main">
            <div class="formulario">
                <h2 style="margin-bottom:20px; color:#f7c775;">Datos del Perfil</h2>

                <?php if ($mensaje): ?>
                    <div class="mensaje"><?= htmlspecialchars($mensaje) ?></div>
                <?php endif; ?>

                <form method="POST">
                    <div class="datos">
                        <label>Nombre:</label><br><br>
                        <input type="text" name="real_name" value="<?= htmlspecialchars($usuario['real_name']) ?>">
                    </div>

                    <div class="datos">
                        <label>Primer apellido:</label><br><br>
                        <input type="text" name="real_surname" value="<?= htmlspecialchars($usuario['real_surname']) ?>">
                    </div>

                    <div class="datos">
                        <label>Correo electrónico:</label><br><br>
                        <input type="email" name="email" value="<?= htmlspecialchars($usuario['email']) ?>">
                    </div>

                    <div class="datos">
                        <label>Nombre de usuario:</label><br><br>
                        <input type="text" name="user_name" value="<?= htmlspecialchars($usuario['user_name']) ?>">
                    </div>

                    <div class="datos">
                        <label>Nueva contraseña (opcional):</label><br><br>
                        <input type="password" name="password" placeholder="Nueva contraseña">
                    </div>

                    <div class="acciones">
                        <button type="submit" name="guardar" class="boton-guardar">Guardar cambios</button>

                        <button type="submit" name="borrar"
                            onclick="return confirm('¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.')"
                            class="boton-borrar">Borrar cuenta</button>
                    </div>
                </form>
            </div>
        </main>

        <aside class="opciones">
            <div class="perfil">
                <div style="margin-left:10px;">
                    <strong><?= htmlspecialchars($_SESSION['user_name']) ?></strong>
                </div>
            </div>

            <ul class="menu">
                <li><a href="./perfil.php">Perfil</a></li>
                <li><a href="./mascotas.php">Mascotas</a></li>
                <li><a href="./vacunas.php">Historial de vacunas</a></li>
            </ul>

            <form method="POST" style="margin-top:20px;">
                <button type="submit" name="logout" class="boton-borrar"
                    style="width:100%; ">
                    Cerrar sesión
                </button>
            </form>

        </aside>

    </div>
</body>

</html>