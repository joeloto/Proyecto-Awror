<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Crear Cuenta - AWROR</title>
<link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&display=swap" rel="stylesheet">
<style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    body, html {
        height: 100%;
        font-family: 'Montserrat', sans-serif;
    }

    .container {
        display: flex;
        width: 100vw;
        height: 100vh;
    }

    .columnaIzquierda {
        flex: 1;
        background: url('https://www.infobae.com/new-resizer/AN9yqvBxbSsBDmcl_bAAV5KBzTY=/arc-anglerfish-arc2-prod-infobae/public/TJMNI2MPI5DCNH6IO45PAMBFEQ.jpg') center/cover no-repeat;
        display: flex;
        justify-content: center;
        align-items: center;
        position: relative;
    }

    .columnaIzquierda .text {
        position: relative;
        z-index: 1;
        text-align: center;
        color: #fff;
        padding: 20px;
    }

    .columnaIzquierda h2 {
        font-size: 32px;
        margin-bottom: 15px;
    }

    .columnaIzquierda p {
        font-size: 18px;
        line-height: 1.5;
    }

    .columnaDerecha {
        flex: 1;
        background-color: #fff8e1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        padding: 50px 40px;
    }

    .columnaDerecha .logo {
        width: 150px;
        margin-bottom: 20px;
    }

    .columnaDerecha h2 {
        color: #8B4513;
        margin-bottom: 10px;
        font-size: 24px;
    }

    .columnaDerecha p {
        color: #A0522D;
        margin-bottom: 30px;
        font-size: 14px;
    }

    .formulario {
        width: 100%;
        margin-bottom: 15px;
    }

    .formulario input {
        width: 500px;
        padding: 14px 15px;
        border-radius: 12px;
        border: 1px solid #D2B48C;
        font-size: 14px;
        transition: all 0.3s;
    }

    .formulario input:focus {
        border-color: #DAA520;
        box-shadow: 0 0 8px rgba(218,165,32,0.5);
        outline: none;
    }

    button {
        width: 100%;
        padding: 14px;
        font-size: 16px;
        font-weight: 600;
        color: #fff;
        background-color: #DAA520; 
        border: none;
        border-radius: 12px;
        cursor: pointer;
        margin-top: 10px;
    }

    button:hover {
        background-color: #B8860B;
    }

    .columnaDerecha a {
        display: block;
        margin-top: 15px;
        font-size: 14px;
        text-decoration: none;
        color: #8B4513;
    }

    .columnaDerecha a:hover {
        color: #B8860B;
    }
</style>
</head>
<body>
<div class="container">
    <div class="columnaIzquierda">
        <div class="text">
            <h2>La aplicación ideal de mascotas</h2>
            <p>Comparte momentos inolvidables con tus queridos compañeros</p>
        </div>
    </div>

    <div class="columnaDerecha">
        <img src="awrorlogo.png" class="logo" alt="Logo">
        <h2>Crear cuenta en AWROR</h2>
        <p>Conviértete en awrorer</p>

        <form action="#" method="post">
            <div class="formulario">
                <input type="text" name="real_name" placeholder="Nombre" required>
            </div>
            <div class="formulario">
                <input type="text" name="real_surname" placeholder="Apellido" required>
            </div>
            <div class="formulario">
                <input type="email" name="email" placeholder="Correo electrónico" required>
            </div>
            <div class="formulario">
                <input type="password" name="password" placeholder="Contraseña" required>
            </div>
            <div class="formulario">
                <input type="text" name="user_name" placeholder="Nombre de usuario" required>
            </div>
            <button type="submit">Crear cuenta</button>
        </form>
        <a href="iniciosesion.php">¿Ya tienes cuenta? Inicia sesión</a>
    </div>
</div>

</body>
</html>
