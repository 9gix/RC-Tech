from .models import Exhibit, Video, Audio, Info 
from django.db import models
from django.contrib import admin

class VideoInline(admin.TabularInline):
    model = Video

class AudioInline(admin.TabularInline):
    model = Audio
    
class InfoInline(admin.TabularInline):
    model = Info
    
class ExhibitAdmin(admin.ModelAdmin):
    fields = ('name',)
    list_display = ('qrcode','name','date')
    inlines = [
        InfoInline, VideoInline, AudioInline,
    ]

admin.site.register(Exhibit,ExhibitAdmin)
