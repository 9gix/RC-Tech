from django.db import models

# Create your models here.

class Exhibit(models.Model):
    qrcode = models.CharField(max_length=32)
    name = models.CharField(max_length=50)
    date = models.DateTimeField(auto_now_add=True)
    
    
class Info(models.Model):
    exhibit = models.ForeignKey(Exhibit)
    type = models.CharField(max_length=20,choices=(('M','main'),('W','wiki'),('D','dictionary'),('B','book'),('R','reference'),('O','others')))
    title = models.CharField(max_length=50)
    link = models.URLField()
    
    
class Video(models.Model):
    exhibit = models.ForeignKey(Exhibit)
    title = models.CharField(max_length=50)
    link = models.URLField()
    
    
class Audio(models.Model):
    exhibit = models.ForeignKey(Exhibit)
    order = models.IntegerField()
    title = models.CharField(max_length=50)
    link = models.URLField()
    
    