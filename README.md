BehAnTweet
==========

Auteurs
-------

*	Alexandre Verkyndt
*	Antonin Carette

Description
-----------

Analyse de comportements avec Twitter (PJE - M1S1)  
Utilisation de Java, l'API Twitter, Twitter4J et Swing

Ce qu'il reste à faire
----------------------

###Fonctionnalités
*	Réalisation du KNN (Antonin)
*	Réalisation de la classification Baysienne (Alexandre)

###IHM
*	Intégration des solutions dans l'interface

###Algorithmique
*	Recherche dichotomique dans le getEvaluationTweet() -> Model.Model

###Specifications
*	valeur de retour KNN : Positif < 0, Indetermine = 0, Negatif > 0

###Structure 
*	Créer un fichier csv qui servira de base d'apprentissage
*   Une base d'apprentissage consiste en un fichier contenant des tweets annotés à la main et qui serviront de base pour la classsification bayésienne.
*	Remarque : Au final, nous aurons deux fichiers : 
*			- le premier étant la base d'apprentissage qui ne pourra être mis à jour qu'à la demande de l'utilisateur(genre une case à coché pour dire que le prochain validate stock les tweets dans la base d'apprentissage et pas l'autre fichier)
*			- le second des tweets annotés par nos différentes méthodes d'évaluations
*	Donc : il serait intéressant de stocké dans le deuxième fichier la méthode qui aura traité le tweet afin de pouvoir les trier