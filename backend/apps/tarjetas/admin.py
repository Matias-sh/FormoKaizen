from django.contrib import admin
from .models import TarjetaRoja, TarjetaImage, TarjetaComment, TarjetaHistory

class TarjetaImageInline(admin.TabularInline):
    model = TarjetaImage
    extra = 0
    readonly_fields = ['uploaded_by', 'uploaded_at']

class TarjetaCommentInline(admin.TabularInline):
    model = TarjetaComment
    extra = 0
    readonly_fields = ['user', 'created_at']

class TarjetaHistoryInline(admin.TabularInline):
    model = TarjetaHistory
    extra = 0
    readonly_fields = ['user', 'timestamp', 'action', 'old_value', 'new_value']

@admin.register(TarjetaRoja)
class TarjetaRojaAdmin(admin.ModelAdmin):
    list_display = ['code', 'title', 'status', 'priority', 'category', 'created_by', 
                   'assigned_to', 'created_at', 'is_overdue']
    list_filter = ['status', 'priority', 'category', 'created_at', 'work_area']
    search_fields = ['title', 'description', 'sector', 'code']
    readonly_fields = ['code', 'created_by', 'approved_by', 'created_at', 'updated_at', 
                      'approved_at', 'closed_at', 'is_overdue', 'days_open']
    inlines = [TarjetaImageInline, TarjetaCommentInline, TarjetaHistoryInline]
    
    fieldsets = (
        ('Información General', {
            'fields': ('code', 'title', 'description', 'category', 'work_area')
        }),
        ('Estado y Prioridad', {
            'fields': ('status', 'priority')
        }),
        ('Detalles del Problema', {
            'fields': ('sector', 'motivo', 'destino_final')
        }),
        ('Asignación y Fechas', {
            'fields': ('created_by', 'assigned_to', 'approved_by', 
                      'estimated_resolution_date', 'actual_resolution_date')
        }),
        ('Resolución', {
            'fields': ('resolution_notes',)
        }),
        ('Metadatos', {
            'fields': ('created_at', 'updated_at', 'approved_at', 'closed_at', 
                      'is_overdue', 'days_open', 'is_active'),
            'classes': ('collapse',)
        }),
    )

@admin.register(TarjetaImage)
class TarjetaImageAdmin(admin.ModelAdmin):
    list_display = ['tarjeta', 'description', 'uploaded_by', 'uploaded_at']
    list_filter = ['uploaded_at', 'uploaded_by']
    readonly_fields = ['uploaded_by', 'uploaded_at']

@admin.register(TarjetaComment)
class TarjetaCommentAdmin(admin.ModelAdmin):
    list_display = ['tarjeta', 'user', 'is_internal', 'created_at']
    list_filter = ['is_internal', 'created_at', 'user']
    readonly_fields = ['user', 'created_at']

@admin.register(TarjetaHistory)
class TarjetaHistoryAdmin(admin.ModelAdmin):
    list_display = ['tarjeta', 'action', 'user', 'timestamp']
    list_filter = ['action', 'timestamp', 'user']
    readonly_fields = ['user', 'timestamp']