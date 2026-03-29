using System;
using System.Net.Http;
using System.Text;
using System.Windows.Forms;
using Newtonsoft.Json;

namespace AWROR
{
    public partial class CrearCuenta : Form
    {
        public CrearCuenta()
        {
            InitializeComponent();
        }

        private void txtEmail_Enter(object sender, EventArgs e)
        {
            if (txtEmail.Text == "Correo electrónico")
                txtEmail.Text = "";
        }

        private void txtEmail_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtEmail.Text))
                txtEmail.Text = "Correo electrónico";
        }

        private void txtPassword_Enter(object sender, EventArgs e)
        {
            if (txtPassword.Text == "Contraseña")
            {
                txtPassword.Text = "";
                txtPassword.PasswordChar = '*';
            }
        }

        private void txtPassword_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtPassword.Text))
            {
                txtPassword.Text = "Contraseña";
                txtPassword.PasswordChar = '\0';
            }
        }

        private void txtNombre_Enter(object sender, EventArgs e)
        {
            if (txtNombre.Text == "Nombre")
                txtNombre.Text = "";
        }

        private void txtNombre_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtNombre.Text))
                txtNombre.Text = "Nombre";
        }

        private void txtApellido_Enter(object sender, EventArgs e)
        {
            if (txtApellido.Text == "Primer apellido")
                txtApellido.Text = "";
        }

        private void txtApellido_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtApellido.Text))
                txtApellido.Text = "Primer apellido";
        }

        private void txtNombreUsuario_Enter(object sender, EventArgs e)
        {
            if (txtNombreUsuario.Text == "Nombre de usuario")
                txtNombreUsuario.Text = "";
        }

        private void txtNombreUsuario_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtNombreUsuario.Text))
                txtNombreUsuario.Text = "Nombre de usuario";
        }

        private void btnYaTengoCuenta_Click(object sender, EventArgs e)
        {
            IniciarSesion f = new IniciarSesion();
            f.Show();
            this.Hide();
        }

        private async void btnCrearCuenta_Click(object sender, EventArgs e)
        {
            if (txtNombre.Text == "Nombre" ||
                txtApellido.Text == "Primer apellido" ||
                txtNombreUsuario.Text == "Nombre de usuario" ||
                txtEmail.Text == "Correo electrónico" ||
                txtPassword.Text == "Contraseña")
            {
                MessageBox.Show("Por favor, completa todos los campos.");
                return;
            }

            var usuario = new
            {
                user_name = txtNombreUsuario.Text,
                real_name = txtNombre.Text,
                real_surname = txtApellido.Text,
                email = txtEmail.Text,
                password = txtPassword.Text
            };

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    string json = JsonConvert.SerializeObject(usuario);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    HttpResponseMessage response = await client.PostAsync("users", content);

                    if (response.StatusCode == System.Net.HttpStatusCode.Conflict)
                    {
                        MessageBox.Show("El correo ya está registrado. Prueba con otro.");
                        return;
                    }

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Cuenta creada correctamente.");
                        IniciarSesion f = new IniciarSesion();
                        f.Show();
                        this.Hide();
                    }
                    else
                    {
                        string error = await response.Content.ReadAsStringAsync();
                        MessageBox.Show("Error al crear la cuenta: " + error);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión con el servidor: " + ex.Message);
            }
        }
    }
}
