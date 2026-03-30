using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace AWROR
{
    public partial class Mascotas : Form
    {
        public Mascotas()
        {
            InitializeComponent();
            btnAgregarMascota.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
            this.WindowState = FormWindowState.Maximized;
            lblUsuario.Text = Sesion.UserName;
            this.Load += async (s, e) => await CargarMascotas();
        }

        private void btnPerfil_Click(object sender, EventArgs e)
        {
            Perfil p = new Perfil();
            p.Show();
            this.Hide();
        }

        private void btnMascotas_Click(object sender, EventArgs e)
        {
            Mascotas m = new Mascotas();
            m.Show();
            this.Hide();
        }


        private void btnVacunas_Click(object sender, EventArgs e)
        {
            Vacunas v = new Vacunas();
            v.Show();
            this.Hide();
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {
            Principal p = new Principal();
            p.Show();
            this.Hide();
        }

        private async void btnAgregarMascota_Click(object sender, EventArgs e)
        {
            MascotaDialog dialog = new MascotaDialog();
            if (dialog.ShowDialog() == DialogResult.OK)
            {
                await SubirMascota(dialog.Nombre, dialog.Tipo, dialog.ImagenSeleccionada);
            }
        }

        private async Task SubirMascota(string nombre, string tipo, Image imagen)
        {
            string base64 = Imagen64(imagen);
            var mascota = new
            {
                user_id = Sesion.UserId,
                name = nombre,
                type = tipo,
                image = base64
            };

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    string json = Newtonsoft.Json.JsonConvert.SerializeObject(mascota);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    HttpResponseMessage response = await client.PostAsync("pets", content);

                    if (response.IsSuccessStatusCode)
                    {
                        Mascota nueva = new Mascota
                        {
                            name = nombre,
                            type = tipo,
                            image = base64
                        };

                        CrearTarjetaMascota(nueva);
                    }
                    else
                    {
                        MessageBox.Show("Error al subir la mascota");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private Image B64ToImage(string base64)
        {
            try
            {
                byte[] bytes = Convert.FromBase64String(base64);
                using (MemoryStream ms = new MemoryStream(bytes))
                {
                    return Image.FromStream(ms);
                }
            }
            catch
            {
                return null;
            }
        }

        private void CrearTarjetaMascota(Mascota mascota)
        {
            Panel tarjeta = new Panel();
            tarjeta.Width = 450;
            tarjeta.Height = 150;
            tarjeta.BackColor = Color.White;
            tarjeta.BorderStyle = BorderStyle.None;

            int anchoPanel = flowLayoutPanelMascotas.ClientSize.Width;
            int margenIzquierdo = (anchoPanel - tarjeta.Width) / 2;
            if (margenIzquierdo < 0)
            {
                margenIzquierdo = 0;
            }

            tarjeta.Margin = new Padding(margenIzquierdo, 20, 0, 10);
            tarjeta.Resize += (s, e) => BordesRedondos(tarjeta, 20);

            PictureBox pic = new PictureBox();
            pic.Width = 100;
            pic.Height = 100;
            pic.Left = 10;
            pic.Top = 10;
            pic.SizeMode = PictureBoxSizeMode.Zoom;

            if (!string.IsNullOrEmpty(mascota.image))
            {
                pic.Image = B64ToImage(mascota.image);
            }

            tarjeta.Controls.Add(pic);

            Label lblNombre = new Label();
            lblNombre.Text = mascota.name;
            lblNombre.Font = new Font("Segoe UI", 12, FontStyle.Bold);
            lblNombre.AutoSize = false;
            lblNombre.Left = pic.Right + 15;
            lblNombre.Top = 20;
            lblNombre.Width = tarjeta.Width - lblNombre.Left - 10;
            lblNombre.Height = 25;

            tarjeta.Controls.Add(lblNombre);

            Label lblTipo = new Label();
            lblTipo.Text = "Animal: " + mascota.type;
            lblTipo.Font = new Font("Segoe UI", 10);
            lblTipo.AutoSize = false;
            lblTipo.Left = pic.Right + 15;
            lblTipo.Top = lblNombre.Bottom + 5;
            lblTipo.Width = tarjeta.Width - lblTipo.Left - 10;
            lblTipo.Height = 20;

            tarjeta.Controls.Add(lblTipo);

            Button btnModificar = new Button();
            btnModificar.Text = "Modificar";
            btnModificar.Width = 100;
            btnModificar.Height = 30;
            btnModificar.Left = pic.Right + 15;
            btnModificar.Top = lblTipo.Bottom + 10;
            btnModificar.BackColor = Color.LightBlue;

            btnModificar.Click += async (s, e) =>
            {
                MascotaDialog dialog = new MascotaDialog(
                    mascota.name,
                    mascota.type,
                    B64ToImage(mascota.image)
                );

                if (dialog.ShowDialog() == DialogResult.OK)
                {
                    await ModificarMascota(
                        mascota.id,
                        dialog.Nombre,
                        dialog.Tipo,
                        dialog.ImagenSeleccionada
                    );

                    await CargarMascotas();
                }
            };

            tarjeta.Controls.Add(btnModificar);

            Button btnEliminar = new Button();
            btnEliminar.Text = "Eliminar";
            btnEliminar.Width = 100;
            btnEliminar.Height = 30;
            btnEliminar.Left = tarjeta.Width - btnEliminar.Width - 10;
            btnEliminar.Top = lblTipo.Bottom + 10;
            btnEliminar.BackColor = Color.LightCoral;

            btnEliminar.Click += async (s, e) =>
            {
                if (MessageBox.Show("¿Seguro que quieres eliminar esta mascota?", "Confirmar", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    await EliminarMascota(mascota.id);
                    await CargarMascotas();
                }
            };

            tarjeta.Controls.Add(btnEliminar);

            flowLayoutPanelMascotas.Controls.Add(tarjeta);
            flowLayoutPanelMascotas.Controls.SetChildIndex(tarjeta, 0);
        }

        private async Task ModificarMascota(int id, string nombre, string tipo, Image imagen)
        {
            string base64 = Imagen64(imagen);

            var mascota = new
            {
                name = nombre,
                type = tipo,
                image = base64
            };

            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                string json = Newtonsoft.Json.JsonConvert.SerializeObject(mascota);
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                HttpResponseMessage response = await client.PutAsync("pets/" + id, content);

                if (!response.IsSuccessStatusCode)
                {
                    string body = await response.Content.ReadAsStringAsync();
                    MessageBox.Show("Error al modificar mascota:\n" + body);
                }
            }
        }

        private async Task EliminarMascota(int id)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                await client.DeleteAsync("pets/" + id);
            }
        }

        private string Imagen64(Image img)
        {
            if (img == null)
            {
                return null;
            }

            using (MemoryStream ms = new MemoryStream())
            {
                using (Bitmap bmp = new Bitmap(img))
                {
                    bmp.Save(ms, System.Drawing.Imaging.ImageFormat.Jpeg);
                    return Convert.ToBase64String(ms.ToArray());
                }
            }

        }

        private async Task CargarMascotas()
        {
            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                    HttpResponseMessage response = await client.GetAsync("pets/user/" + Sesion.UserId);
                    string json = await response.Content.ReadAsStringAsync();
                    if (response.IsSuccessStatusCode)
                    {
                        List<Mascota> mascotas = Newtonsoft.Json.JsonConvert.DeserializeObject<List<Mascota>>(json);
                        flowLayoutPanelMascotas.Controls.Clear();
                        foreach (var m in mascotas)
                        {
                            CrearTarjetaMascota(m);
                        }
                    }
                    else
                    {
                        MessageBox.Show("Error al cargar mascotas\nStatus: " + (int)response.StatusCode + "\nBody: " + json);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private void BordesRedondos(Panel panel, int radio)
        {
            GraphicsPath path = new GraphicsPath();
            path.StartFigure();
            path.AddArc(new Rectangle(0, 0, radio, radio), 180, 90);
            path.AddArc(new Rectangle(panel.Width - radio, 0, radio, radio), 270, 90);
            path.AddArc(new Rectangle(panel.Width - radio, panel.Height - radio, radio, radio), 0, 90);
            path.AddArc(new Rectangle(0, panel.Height - radio, radio, radio), 90, 90);
            path.CloseFigure();
            panel.Region = new Region(path);
        }

    }
}
