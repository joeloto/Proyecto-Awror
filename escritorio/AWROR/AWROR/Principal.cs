using System;
using System.Collections.Generic;
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
    public partial class Principal : Form
    {
        public Principal()
        {
            InitializeComponent();
            btnPublicar.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
            this.WindowState = FormWindowState.Maximized;
            lblUsuario.Text = Sesion.UserName;
            this.AutoScaleMode = AutoScaleMode.Dpi;
            this.AutoSizeMode = AutoSizeMode.GrowAndShrink;
            this.Load += async (s, e) => await CargarPosts();
        }

        private void btnPerfil_Click(object sender, EventArgs e)
        {
            new Perfil().Show();
            this.Hide();
        }

        private void btnMascotas_Click(object sender, EventArgs e)
        {
            new Mascotas().Show();
            this.Hide();
        }

        private void btnVacunas_Click(object sender, EventArgs e)
        {
            new Vacunas().Show();
            this.Hide();
        }

        private async void btnPublicar_Click(object sender, EventArgs e)
        {
            PublicarDialog dialog = new PublicarDialog();

            if (dialog.ShowDialog() == DialogResult.OK)
            {
                await SubirPost(dialog.TextoPost, dialog.ImagenPost);
            }
        }

        private void TarjetaBd(Post post)
        {
            string usuario = post.username;
            string texto = post.contenido;
            Image imagen = B64ToImage(post.imagen);

            Panel tarjeta = new Panel();
            tarjeta.Width = 500;
            tarjeta.Height = imagen == null ? 190 : 420;
            tarjeta.BackColor = Color.White;
            tarjeta.BorderStyle = BorderStyle.None;

            int anchoPanel = flowLayoutPanelPosts.ClientSize.Width;
            int margenIzquierdo = (anchoPanel - tarjeta.Width) / 2;
            if (margenIzquierdo < 0)
            {
                margenIzquierdo = 0;
            }
            tarjeta.Margin = new Padding(margenIzquierdo, 30, 0, 10);
            tarjeta.Resize += (s, e) => BordesRedondos(tarjeta, 20);

            Label lblUsuario = new Label();
            lblUsuario.Text = usuario;
            lblUsuario.Font = new Font("Segoe UI", 11, FontStyle.Bold);
            lblUsuario.AutoSize = false;
            lblUsuario.Width = tarjeta.Width;
            lblUsuario.Height = 30;
            lblUsuario.Top = 10;
            lblUsuario.Left = 10;

            Label lblTexto = new Label();
            lblTexto.Text = texto;
            lblTexto.Font = new Font("Segoe UI", 10);
            lblTexto.AutoSize = false;
            lblTexto.Width = tarjeta.Width - 20;
            lblTexto.Height = imagen == null ? 80 : 60;
            lblTexto.Top = lblUsuario.Bottom + 5;
            lblTexto.Left = 10;

            tarjeta.Controls.Add(lblUsuario);
            tarjeta.Controls.Add(lblTexto);

            PictureBox pic = null;
            int bottomY = lblTexto.Bottom;

            if (imagen != null)
            {
                pic = new PictureBox();
                pic.Image = imagen;
                pic.SizeMode = PictureBoxSizeMode.StretchImage;
                pic.Width = tarjeta.Width - 20;
                pic.Height = 220;
                pic.Top = lblTexto.Bottom + 10;
                pic.Left = 10;
                tarjeta.Controls.Add(pic);
                bottomY = pic.Bottom;
            }

            if (post.user_id == Sesion.UserId)
            {
                Button btnModificar = new Button();
                btnModificar.Text = "Modificar";
                btnModificar.Width = 100;
                btnModificar.Height = 30;
                btnModificar.Left = 10;
                btnModificar.Top = bottomY + 10;
                btnModificar.BackColor = Color.LightBlue;

                btnModificar.Click += async (s, e) =>
                {
                    PublicarDialog dialog = new PublicarDialog(post.contenido, B64ToImage(post.imagen));

                    if (dialog.ShowDialog() == DialogResult.OK)
                    {
                        await ModificarPost(post.id, dialog.TextoPost, dialog.ImagenPost);
                        await CargarPosts();
                    }
                };

                Button btnEliminar = new Button();
                btnEliminar.Text = "Eliminar";
                btnEliminar.Width = 100;
                btnEliminar.Height = 30;
                btnEliminar.Left = tarjeta.Width - btnEliminar.Width - 10;
                btnEliminar.Top = bottomY + 10;
                btnEliminar.BackColor = Color.Red;

                btnEliminar.Click += async (s, e) =>
                {
                    if (MessageBox.Show("¿Seguro que quieres eliminar esta publicación?", "Confirmar", MessageBoxButtons.YesNo) == DialogResult.Yes)
                    {
                        await EliminarPost(post.id);
                        await CargarPosts();
                    }
                };

                tarjeta.Controls.Add(btnModificar);
                tarjeta.Controls.Add(btnEliminar);
            }

            flowLayoutPanelPosts.SuspendLayout();
            flowLayoutPanelPosts.Controls.Add(tarjeta);
            flowLayoutPanelPosts.Controls.SetChildIndex(tarjeta, 0);
            flowLayoutPanelPosts.ResumeLayout();


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

        public Image B64ToImage(string base64)
        {
            if (string.IsNullOrEmpty(base64))
            {
                return null;
            }

            byte[] bytes = Convert.FromBase64String(base64);
            using (MemoryStream ms = new MemoryStream(bytes))
            {
                return Image.FromStream(ms);
            }
        }

        public string Imagen64(Image img)
        {
            if (img == null)
            {
                return null;
            }

            using (MemoryStream ms = new MemoryStream())
            {
                img.Save(ms, System.Drawing.Imaging.ImageFormat.Jpeg);
                return Convert.ToBase64String(ms.ToArray());
            }
        }

        private async Task SubirPost(string texto, Image imagen)
        {
            string base64 = Imagen64(imagen);

            var post = new
            {
                user_id = Sesion.UserId,
                contenido = texto,
                imagen = base64
            };

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    string json = Newtonsoft.Json.JsonConvert.SerializeObject(post);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    HttpResponseMessage response = await client.PostAsync("posts", content);

                    if (response.IsSuccessStatusCode)
                    {
                        await CargarPosts();
                    }
                    else
                    {
                        MessageBox.Show("Error al subir el post");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private async Task ModificarPost(int id, string texto, Image imagen)
        {
            string base64 = Imagen64(imagen);

            var post = new
            {
                user_id = Sesion.UserId,
                contenido = texto,
                imagen = base64
            };

            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                string json = Newtonsoft.Json.JsonConvert.SerializeObject(post);
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                HttpResponseMessage response = await client.PutAsync("posts/" + id, content);

                if (!response.IsSuccessStatusCode)
                {
                    string body = await response.Content.ReadAsStringAsync();
                    MessageBox.Show("Error al modificar post:\n" + body);
                }
            }
        }

        private async Task EliminarPost(int id)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                await client.DeleteAsync("posts/" + id);
            }
        }

        private async Task CargarPosts()
        {
            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                    HttpResponseMessage response = await client.GetAsync("posts");

                    if (response.IsSuccessStatusCode)
                    {
                        string json = await response.Content.ReadAsStringAsync();
                        List<Post> posts = Newtonsoft.Json.JsonConvert.DeserializeObject<List<Post>>(json);

                        flowLayoutPanelPosts.Controls.Clear();

                        foreach (var p in posts.OrderBy(x => x.id))
                        {
                            TarjetaBd(p);
                        }
                    }
                    else
                    {
                        MessageBox.Show("Error al cargar publicaciones");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }
    }
}
