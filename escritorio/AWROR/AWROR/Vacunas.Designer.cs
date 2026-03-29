namespace AWROR
{
    partial class Vacunas
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Vacunas));
            this.panel1 = new System.Windows.Forms.Panel();
            this.cmbMascotas = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.btnVacunas = new System.Windows.Forms.Button();
            this.btnMascotas = new System.Windows.Forms.Button();
            this.btnPerfil = new System.Windows.Forms.Button();
            this.panel2 = new System.Windows.Forms.Panel();
            this.btnAgregarVacuna = new System.Windows.Forms.Button();
            this.lblUsuario = new System.Windows.Forms.Label();
            this.flowLayoutPanelVacunas = new System.Windows.Forms.FlowLayoutPanel();
            this.panel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            this.panel2.SuspendLayout();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.SystemColors.Control;
            this.panel1.Controls.Add(this.cmbMascotas);
            this.panel1.Controls.Add(this.label1);
            this.panel1.Controls.Add(this.pictureBox1);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel1.Location = new System.Drawing.Point(0, 0);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(800, 77);
            this.panel1.TabIndex = 5;
            // 
            // cmbMascotas
            // 
            this.cmbMascotas.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.cmbMascotas.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cmbMascotas.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 13.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.cmbMascotas.FormattingEnabled = true;
            this.cmbMascotas.Location = new System.Drawing.Point(594, 20);
            this.cmbMascotas.Name = "cmbMascotas";
            this.cmbMascotas.Size = new System.Drawing.Size(256, 39);
            this.cmbMascotas.TabIndex = 7;
            // 
            // label1
            // 
            this.label1.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(84, 20);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(184, 37);
            this.label1.TabIndex = 1;
            this.label1.Text = "AWROR";
            this.label1.Click += new System.EventHandler(this.pictureBox1_Click);
            // 
            // pictureBox1
            // 
            this.pictureBox1.Image = global::AWROR.Properties.Resources.awrorlogo;
            this.pictureBox1.Location = new System.Drawing.Point(0, 0);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(78, 77);
            this.pictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.pictureBox1.TabIndex = 0;
            this.pictureBox1.TabStop = false;
            this.pictureBox1.Click += new System.EventHandler(this.pictureBox1_Click);
            // 
            // btnVacunas
            // 
            this.btnVacunas.FlatAppearance.BorderSize = 0;
            this.btnVacunas.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnVacunas.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnVacunas.Location = new System.Drawing.Point(16, 310);
            this.btnVacunas.Name = "btnVacunas";
            this.btnVacunas.Size = new System.Drawing.Size(183, 51);
            this.btnVacunas.TabIndex = 3;
            this.btnVacunas.Text = "Vacunas";
            this.btnVacunas.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnVacunas.UseVisualStyleBackColor = true;
            this.btnVacunas.Click += new System.EventHandler(this.btnVacunas_Click);
            // 
            // btnMascotas
            // 
            this.btnMascotas.FlatAppearance.BorderSize = 0;
            this.btnMascotas.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnMascotas.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnMascotas.Location = new System.Drawing.Point(16, 188);
            this.btnMascotas.Name = "btnMascotas";
            this.btnMascotas.Size = new System.Drawing.Size(200, 51);
            this.btnMascotas.TabIndex = 2;
            this.btnMascotas.Text = "Mascotas";
            this.btnMascotas.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnMascotas.UseVisualStyleBackColor = true;
            this.btnMascotas.Click += new System.EventHandler(this.btnMascotas_Click);
            // 
            // btnPerfil
            // 
            this.btnPerfil.BackColor = System.Drawing.SystemColors.Control;
            this.btnPerfil.FlatAppearance.BorderSize = 0;
            this.btnPerfil.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnPerfil.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnPerfil.Location = new System.Drawing.Point(16, 75);
            this.btnPerfil.Name = "btnPerfil";
            this.btnPerfil.Size = new System.Drawing.Size(136, 48);
            this.btnPerfil.TabIndex = 1;
            this.btnPerfil.Text = "Perfil";
            this.btnPerfil.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnPerfil.UseVisualStyleBackColor = false;
            this.btnPerfil.Click += new System.EventHandler(this.btnPerfil_Click);
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.btnAgregarVacuna);
            this.panel2.Controls.Add(this.btnVacunas);
            this.panel2.Controls.Add(this.btnMascotas);
            this.panel2.Controls.Add(this.btnPerfil);
            this.panel2.Controls.Add(this.lblUsuario);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Right;
            this.panel2.Location = new System.Drawing.Point(500, 77);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(300, 373);
            this.panel2.TabIndex = 8;
            // 
            // btnAgregarVacuna
            // 
            this.btnAgregarVacuna.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnAgregarVacuna.Location = new System.Drawing.Point(28, 286);
            this.btnAgregarVacuna.Name = "btnAgregarVacuna";
            this.btnAgregarVacuna.Size = new System.Drawing.Size(245, 75);
            this.btnAgregarVacuna.TabIndex = 9;
            this.btnAgregarVacuna.Text = "Agregar vacuna";
            this.btnAgregarVacuna.UseVisualStyleBackColor = true;
            this.btnAgregarVacuna.Click += new System.EventHandler(this.btnAgregarVacuna_Click);
            // 
            // lblUsuario
            // 
            this.lblUsuario.AutoSize = true;
            this.lblUsuario.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 10.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblUsuario.Location = new System.Drawing.Point(23, 23);
            this.lblUsuario.Name = "lblUsuario";
            this.lblUsuario.Size = new System.Drawing.Size(78, 25);
            this.lblUsuario.TabIndex = 0;
            this.lblUsuario.Text = "label2";
            // 
            // flowLayoutPanelVacunas
            // 
            this.flowLayoutPanelVacunas.AutoScroll = true;
            this.flowLayoutPanelVacunas.BackgroundImage = global::AWROR.Properties.Resources.fondoawror2;
            this.flowLayoutPanelVacunas.Dock = System.Windows.Forms.DockStyle.Fill;
            this.flowLayoutPanelVacunas.Location = new System.Drawing.Point(0, 77);
            this.flowLayoutPanelVacunas.Name = "flowLayoutPanelVacunas";
            this.flowLayoutPanelVacunas.Size = new System.Drawing.Size(500, 373);
            this.flowLayoutPanelVacunas.TabIndex = 9;
            // 
            // Vacunas
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackgroundImage = global::AWROR.Properties.Resources.fondoawror2;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.flowLayoutPanelVacunas);
            this.Controls.Add(this.panel2);
            this.Controls.Add(this.panel1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Vacunas";
            this.Text = "Vacunas de las mascotas";
            this.panel1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            this.panel2.ResumeLayout(false);
            this.panel2.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.ComboBox cmbMascotas;
        private System.Windows.Forms.Button btnVacunas;
        private System.Windows.Forms.Button btnMascotas;
        private System.Windows.Forms.Button btnPerfil;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Label lblUsuario;
        private System.Windows.Forms.Button btnAgregarVacuna;
        private System.Windows.Forms.FlowLayoutPanel flowLayoutPanelVacunas;
    }
}