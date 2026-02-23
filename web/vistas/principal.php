<?php
session_start();

if (!isset($_SESSION['id'])) {
    header("Location: iniciosesion.php");
    exit();
}

$usuario_id = $_SESSION['id'];
$api = "http://localhost:8080/apiawror/rest/posts";

/* -------- CREAR POST -------- */
if (isset($_POST['publicar'])) {

    $data = [
        "user_id" => $usuario_id,
        "contenido" => $_POST['contenido']
    ];

    $ch = curl_init($api);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        'Content-Type: application/json'
    ]);
    curl_exec($ch);
    curl_close($ch);

    header("Location: principal.php");
    exit();
}

/* -------- ELIMINAR POST -------- */
if (isset($_POST['eliminar'])) {

    $post_id = $_POST['post_id'];

    $ch = curl_init($api . "/" . $post_id);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_exec($ch);
    curl_close($ch);

    header("Location: principal.php");
    exit();
}

/* -------- OBTENER POSTS -------- */
$ch = curl_init($api . "/user/" . $usuario_id);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$response = curl_exec($ch);
curl_close($ch);

$posts = json_decode($response, true);
?>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AWROR</title>

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
        }

        header {
            grid-column: 1 / 3;
            background-color: #242526;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
            border-bottom: 1px solid #3a3b3c;
            position: sticky;
            top: 0;
        }

        .logo {
            display: flex;
            align-items: center;
            text-decoration: none;
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
            font-weight: bold;
            font-size: 25px;
        }

        main {
            padding: 40px;
            display: flex;
            justify-content: center;
            overflow-y: auto;
        }

        .publicacion {
            background-color: #242526;
            width: 600px;
            height: 485px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
            padding: 20px;
            border: 1px solid #3a3b3c;
            /* display: none; */
        }

        .cabecera {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .fotoperfil {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            background-color: #3a3b3c;
            margin-right: 12px;
        }

        .nombre-usuario {
            font-weight: 600;
            color: #e4e6eb;
        }

        .contenidop {
            margin: 15px 0;
            font-size: 15px;
            line-height: 1.5;
            color: #d0d2d6;
        }

        .imagen {
            width: 100%;
            height: 300px;
            background-color: #3a3b3c;
            border-radius: 10px;
        }

        .acciones {
            display: flex;
            justify-content: space-around;
            margin-top: 15px;
            padding-top: 10px;
            border-top: 1px solid #3a3b3c;
        }

        .boton-accion {
            cursor: pointer;
            color: #b0b3b8;
            font-weight: 500;
            transition: 0.2s;
        }

        .boton-accion:hover {
            color: #f7c775;
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
        }

        .menu {
            list-style: none;
            padding: 0;
        }

        .menu li {
            margin-bottom: 5px;
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

        .menu li:hover {
            background-color: #3a3b3c;
        }

        .botonpublicar {
            position: fixed;
            bottom: 30px;
            left: 30px;
            width: 100px;
            height: 60px;
            border-radius: 10px;
            border: none;
            background-color: #f7c775;
            color: white;
            font-size: 20px;
            font-weight: bold;
            cursor: pointer;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
            display: inline-block;
            line-height: 60px;
            text-align: center;
            vertical-align: middle;
            transition: background-color 0.3s, transform 0.2s;
        }

        .botonpublicar:hover {
            background-color: #ea9d55;
            transform: scale(1.1);
        }

        .boton-eliminar {
            background-color: #b02a37;
            color: white;
            border: none;
            padding: 8px 14px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: 0.2s;
        }

        .boton-eliminar:hover {
            background-color: #e03141;
            transform: scale(1.05);
        }

        .boton-eliminar:active {
            transform: scale(0.95);
        }
    </style>
</head>
<script>
    function mostrarFormulario() {
        const form = document.getElementById("formPublicar");
        form.style.display = form.style.display === "none" ? "block" : "none";
    }
</script>

<body>

    <div class="pagina">
        <header>
            <div class="logo"><a href="./principal.php"><img src="../awrorlogo.png" width="50px" height="50px"></a></div>

            <div class="tituloapp">
                <a href="./principal.php">AWROR</a>
            </div>
        </header>
        <main>
            <div style="width:600px;">
                <div id="formPublicar" style="display:none; margin-bottom:20px;">
                    <form method="POST">
                        <textarea name="contenido" required
                            style="width:100%; height:100px; padding:10px; border-radius:8px; border:none;"></textarea>
                        <button type="submit" name="publicar"
                            style="margin-top:10px; padding:10px 15px; border:none; border-radius:8px; background:#f7c775; cursor:pointer;">
                            <strong>Publicar</strong>
                        </button>
                    </form>
                </div>

                <?php if (!empty($posts)): ?>
                    <?php foreach ($posts as $post): ?>
                        <div class="publicacion" style="height:auto; margin-bottom:20px;">
                            <div class="cabecera">
                                <div class="fotoperfil"></div>
                                <div>
                                    <div class="nombre-usuario">
                                        <?= htmlspecialchars($_SESSION['user_name'] ?? 'Usuario') ?>
                                    </div>
                                </div>
                            </div>

                            <div class="contenidop">
                                <?= htmlspecialchars($post['contenido']) ?>
                            </div>

                            <div class="acciones">
                                <form method="POST"
                                    onsubmit="return confirm('¿Seguro que quieres eliminar esta publicación?');">
                                    <input type="hidden" name="post_id" value="<?= $post['id'] ?>">
                                    <button type="submit" name="eliminar" class="boton-eliminar">
                                        Eliminar publicación
                                    </button>
                                </form>
                            </div>
                        </div>
                    <?php endforeach; ?>
                <?php else: ?>
                    <p style="text-align:center; color:#b0b3b8;">
                        No hay publicaciones todavía.
                    </p>
                <?php endif; ?>

            </div>
        </main>
        <aside class="opciones">
            <div class="perfil">
                <div class="fotoperfil"></div>
                <div style="margin-left:10px;">
                <strong><?= htmlspecialchars($_SESSION['user_name']) ?></strong>
                </div>
            </div>
            <ul class="menu">
                <li><a href="./perfil.php">Perfil</a></li>
                <li><a href="#">Mascotas</a></li>
                <li><a href="#">Historial de vacunas</a></li>
                <li><a href="#">Calendario</a></li>
            </ul>
        </aside>
    </div>
    <button class="botonpublicar" onclick="mostrarFormulario()">
        Publicar
    </button>
</body>

</html>