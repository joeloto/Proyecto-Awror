using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace AWROR
{
    public partial class PublicarDialog : Form
    {
        public string TextoPost { get; private set; }
        public Image ImagenPost { get; private set; }

        public PublicarDialog()
        {
            InitializeComponent();
        }

        public PublicarDialog(string texto, Image imagen)
        {
            InitializeComponent();
            txtContenido.Text = texto;
            pbPreFoto.Image = imagen;
        }

        private void btnSeleccionarImagen_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.Filter = "Imágenes|*.jpg;*.png;*.jpeg";

            if (ofd.ShowDialog() == DialogResult.OK)
            {
                ImagenPost = Image.FromFile(ofd.FileName);
                pbPreFoto.Image = ImagenPost;
            }
        }

        private void btnPublicar_Click(object sender, EventArgs e)
        {
            TextoPost = txtContenido.Text;
            this.DialogResult = DialogResult.OK;
            this.Close();
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }
    }

}
