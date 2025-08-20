from django.db import models
from django.contrib.auth import get_user_model
from apps.categories.models import Category, WorkArea

User = get_user_model()

class TarjetaRoja(models.Model):
    STATUS_CHOICES = [
        ('open', 'Abierta'),
        ('pending_approval', 'Pendiente de Aprobación'),
        ('approved', 'Aprobada'),
        ('in_progress', 'En Progreso'),
        ('resolved', 'Resuelta'),
        ('closed', 'Cerrada'),
        ('rejected', 'Rechazada'),
    ]
    
    PRIORITY_CHOICES = [
        ('low', 'Baja'),
        ('medium', 'Media'),
        ('high', 'Alta'),
        ('critical', 'Crítica'),
    ]
    
    # Información básica
    title = models.CharField(max_length=200, verbose_name='Título')
    description = models.TextField(verbose_name='Descripción del problema')
    category = models.ForeignKey(Category, on_delete=models.CASCADE, 
                                related_name='tarjetas', verbose_name='Categoría')
    work_area = models.ForeignKey(WorkArea, on_delete=models.CASCADE, null=True, blank=True,
                                 related_name='tarjetas', verbose_name='Área de trabajo')
    
    # Estados y prioridad
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='open')
    priority = models.CharField(max_length=20, choices=PRIORITY_CHOICES, default='medium')
    
    # Usuarios involucrados
    created_by = models.ForeignKey(User, on_delete=models.CASCADE, 
                                  related_name='created_tarjetas')
    assigned_to = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True,
                                   related_name='assigned_tarjetas', 
                                   verbose_name='Asignado a')
    approved_by = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True,
                                   related_name='approved_tarjetas',
                                   verbose_name='Aprobado por')
    
    # Campos originales adaptados
    sector = models.CharField(max_length=100, blank=True, verbose_name='Sector específico')
    motivo = models.TextField(verbose_name='Motivo/Causa raíz')
    destino_final = models.TextField(verbose_name='Acción correctiva propuesta')
    
    # Seguimiento
    estimated_resolution_date = models.DateField(null=True, blank=True,
                                               verbose_name='Fecha estimada de resolución')
    actual_resolution_date = models.DateField(null=True, blank=True,
                                            verbose_name='Fecha real de resolución')
    resolution_notes = models.TextField(blank=True, verbose_name='Notas de resolución')
    
    # Metadatos
    is_active = models.BooleanField(default=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    approved_at = models.DateTimeField(null=True, blank=True)
    closed_at = models.DateTimeField(null=True, blank=True)
    
    class Meta:
        db_table = 'tarjetas_rojas'
        verbose_name = 'Tarjeta Roja'
        verbose_name_plural = 'Tarjetas Rojas'
        ordering = ['-created_at']
    
    def __str__(self):
        return f"TR-{self.id:04d} - {self.title}"
    
    @property
    def code(self):
        return f"TR-{self.id:04d}"
    
    @property
    def is_overdue(self):
        if self.estimated_resolution_date and self.status not in ['resolved', 'closed']:
            from django.utils import timezone
            return timezone.now().date() > self.estimated_resolution_date
        return False
    
    @property
    def days_open(self):
        from django.utils import timezone
        if self.status in ['resolved', 'closed'] and self.closed_at:
            end_date = self.closed_at.date()
        else:
            end_date = timezone.now().date()
        return (end_date - self.created_at.date()).days
    
    def can_be_approved_by(self, user):
        return user.can_approve_tarjetas() and self.status == 'pending_approval'
    
    def can_be_edited_by(self, user):
        return (user == self.created_by and self.status in ['open', 'rejected']) or user.is_supervisor()

class TarjetaImage(models.Model):
    tarjeta = models.ForeignKey(TarjetaRoja, on_delete=models.CASCADE, related_name='images')
    image = models.ImageField(upload_to='tarjetas/%Y/%m/%d/')
    description = models.CharField(max_length=200, blank=True)
    uploaded_by = models.ForeignKey(User, on_delete=models.CASCADE)
    uploaded_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'tarjeta_images'
        verbose_name = 'Imagen de Tarjeta'
        verbose_name_plural = 'Imágenes de Tarjetas'
    
    def __str__(self):
        return f"Imagen de {self.tarjeta.code}"

class TarjetaComment(models.Model):
    tarjeta = models.ForeignKey(TarjetaRoja, on_delete=models.CASCADE, related_name='comments')
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    comment = models.TextField()
    is_internal = models.BooleanField(default=False, verbose_name='Comentario interno')
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'tarjeta_comments'
        verbose_name = 'Comentario'
        verbose_name_plural = 'Comentarios'
        ordering = ['-created_at']
    
    def __str__(self):
        return f"Comentario en {self.tarjeta.code} por {self.user.full_name}"

class TarjetaHistory(models.Model):
    tarjeta = models.ForeignKey(TarjetaRoja, on_delete=models.CASCADE, related_name='history')
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    action = models.CharField(max_length=50)
    old_value = models.CharField(max_length=200, blank=True)
    new_value = models.CharField(max_length=200, blank=True)
    timestamp = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'tarjeta_history'
        verbose_name = 'Historial'
        verbose_name_plural = 'Historial'
        ordering = ['-timestamp']
    
    def __str__(self):
        return f"{self.tarjeta.code} - {self.action} por {self.user.full_name}"