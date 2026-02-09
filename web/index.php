<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear cuenta</title>
    <style>
        body {
            background-image: url('https://images.alphacoders.com/130/1307447.jpg');
            background-size: cover;
        }

        input {
            width: 450px;
            height: 50px;
            border-radius: 10px;
            font-size: 20px;
        }

        section{
            display: flex;
            align-content: center;
            justify-content: center;
        }
    </style>
</head>

<body>
    <div>
        <section>
            <form action="#">
                <input type="text" name="nombre" id="nombre" placeholder="Nombre"><br><br>
                <input type="text" name="apellidos" id="apellidos" placeholder="Apellidos"><br><br>
                <input type="email" name="email" id="email" placeholder="Email"><br><br>
                <input type="password" name="password" id="password" placeholder="Contraseña"><br><br>
                <input type="text" name="usuario" id="usuario" placeholder="Nombre de usuario"><br><br>
            </form>
        </section>
    </div>
</body>

</html>