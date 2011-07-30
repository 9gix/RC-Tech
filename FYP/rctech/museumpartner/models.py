from django.db import models

# Create your models here.

class Exhibit(models.Model):
    name = models.CharField(max_length=100)
    date = models.DateTimeField(auto_now_add=True)
    
class Info(models.Model):
    exhibit = models.ForeignKey(Exhibit)
    title = models.CharField(max_length=50)
    link = models.URLField()
    
    
class Video(models.Model):
    exhibit = models.ForeignKey(Exhibit)
    title = models.CharField(max_length=50)
    link = models.URLField()
    
    
class Audio(models.Model):
    exhibit = models.ForeignKey(Exhibit)
    title = models.CharField(max_length=50)
    link = models.URLField()
    
    