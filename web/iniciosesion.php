<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio de Sesión - AWROR</title>
    <style>

        body {
            font-family: Arial, sans-serif;
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            transition: background-image 1s ease-in-out;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .formulario {
            position: relative; 
            background-color: rgba(255, 248, 225, 0.75); 
            padding: 40px 30px;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.3);
            width: 350px;
            text-align: center;
        }

        .formulario .logo {
            position: absolute;
            top: 10px;
            left: 10px;
            width: 60px;
            height: 60px;
            border-radius: 5px;
        }

        .formulario h1 {
            color: #8B4513; 
            margin-bottom: 10px;
        }

        .formulario p {
            color: #A0522D; 
            margin-bottom: 30px;
            font-weight: bold;
        }

        .formulario input[type="email"],
        .formulario input[type="password"] {
            width: 93%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #D2B48C; 
            border-radius: 8px;
            font-size: 16px;
        }

        .formulario button {
            width: 100%;
            padding: 12px;
            background-color: #DAA520; 
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .formulario button:hover {
            background-color: #B8860B;
        }

        .formulario a {
            display: block;
            margin-top: 20px;
            color: #8B4513;
            text-decoration: none;
        }

        .formulario a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="formulario">
        <img src="awrorlogo.png" alt="Logo" class="logo">

        <h1>Bienvenido a AWROR</h1>
        <p>Inicia sesión en tu cuenta</p>
        <form action="#" method="post">
            <input type="email" name="email" placeholder="Correo electrónico" required >
            <input type="password" name="password" placeholder="Contraseña" required>
            <button type="submit">Acceder</button>
        </form>
        <a href="index.php">¿No tienes cuenta? Crear cuenta</a>
    </div>

    <script>
        const backgrounds = [
            'https://images.alphacoders.com/130/1307447.jpg',
            'https://wallpapers.com/images/featured/animales-bebes-wmsvikfmzvhh3aot.jpg',
        ];

        let current = 0;

        function changeBackground() {
            current = (current + 1) % backgrounds.length;
            document.body.style.backgroundImage = `url('${backgrounds[current]}')`;
        }

        document.body.style.backgroundImage = `url('${backgrounds[0]}')`;

        setInterval(changeBackground, 5000);
    </script>

</body>
</html>
