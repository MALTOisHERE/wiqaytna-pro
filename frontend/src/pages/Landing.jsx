import React from 'react';
import { useNavigate } from 'react-router-dom';
import { FiCalendar, FiUsers, FiClock, FiCheckCircle, FiShield, FiBell, FiActivity, FiHeart } from 'react-icons/fi';

/**
 * Landing Page Component
 * Professional medical-themed landing page for Wiqaytna Pro
 */
const Landing = () => {
  const navigate = useNavigate();

  const features = [
    {
      icon: <FiCalendar size={32} />,
      title: 'Gestion des rendez-vous',
      description: 'Planifiez et gérez vos consultations médicales en toute simplicité'
    },
    {
      icon: <FiUsers size={32} />,
      title: 'Gestion des patients',
      description: 'Accès rapide aux dossiers médicaux et informations des patients'
    },
    {
      icon: <FiClock size={32} />,
      title: 'Disponibilité en temps réel',
      description: 'Consultez les créneaux disponibles et prenez rendez-vous instantanément'
    },
    {
      icon: <FiBell size={32} />,
      title: 'Rappels automatiques',
      description: 'Notifications SMS pour ne jamais manquer un rendez-vous'
    },
    {
      icon: <FiShield size={32} />,
      title: 'Sécurité des données',
      description: 'Protection complète de vos informations médicales confidentielles'
    },
    {
      icon: <FiActivity size={32} />,
      title: 'Suivi médical',
      description: 'Historique complet des consultations et antécédents médicaux'
    }
  ];

  const stats = [
    { value: '500+', label: 'Médecins' },
    { value: '10,000+', label: 'Patients' },
    { value: '50,000+', label: 'Rendez-vous' },
    { value: '98%', label: 'Satisfaction' }
  ];

  return (
    <div className="landing-page">
      {/* Header */}
      <header className="landing-header">
        <div className="container">
          <div className="logo">
            <FiHeart size={32} className="logo-icon" />
            <h1>Wiqaytna Pro</h1>
          </div>
          <nav className="landing-nav">
            <button onClick={() => navigate('/login')} className="btn-secondary">
              Connexion
            </button>
            <button onClick={() => navigate('/register')} className="btn-primary">
              Inscription
            </button>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <section className="hero-section">
        <div className="container">
          <div className="hero-content">
            <div className="hero-text">
              <h1 className="hero-title">
                La gestion de votre cabinet médical
                <span className="highlight"> simplifiée</span>
              </h1>
              <p className="hero-description">
                Wiqaytna Pro est la solution complète pour la gestion des rendez-vous médicaux au Maroc.
                Modernisez votre pratique médicale et offrez une meilleure expérience à vos patients.
              </p>
              <div className="hero-buttons">
                <button onClick={() => navigate('/register')} className="btn-primary btn-large">
                  Commencer gratuitement
                </button>
                <button className="btn-secondary btn-large">
                  En savoir plus
                </button>
              </div>
            </div>
            <div className="hero-image">
              <div className="hero-card">
                <FiActivity size={48} className="hero-card-icon" />
                <h3>Plateforme médicale professionnelle</h3>
                <p>Conçue spécialement pour les cabinets médicaux marocains</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="stats-section">
        <div className="container">
          <div className="stats-grid">
            {stats.map((stat, index) => (
              <div key={index} className="stat-card">
                <div className="stat-value">{stat.value}</div>
                <div className="stat-label">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <div className="section-header">
            <h2>Fonctionnalités complètes</h2>
            <p>Tout ce dont vous avez besoin pour gérer votre pratique médicale</p>
          </div>
          <div className="features-grid">
            {features.map((feature, index) => (
              <div key={index} className="feature-card">
                <div className="feature-icon">{feature.icon}</div>
                <h3>{feature.title}</h3>
                <p>{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Benefits Section */}
      <section className="benefits-section">
        <div className="container">
          <div className="benefits-content">
            <div className="benefits-text">
              <h2>Pourquoi choisir Wiqaytna Pro ?</h2>
              <ul className="benefits-list">
                <li>
                  <FiCheckCircle className="check-icon" />
                  <span>Interface intuitive et facile à utiliser</span>
                </li>
                <li>
                  <FiCheckCircle className="check-icon" />
                  <span>Accessible depuis n'importe quel appareil</span>
                </li>
                <li>
                  <FiCheckCircle className="check-icon" />
                  <span>Support en français et arabe</span>
                </li>
                <li>
                  <FiCheckCircle className="check-icon" />
                  <span>Conforme aux normes médicales marocaines</span>
                </li>
                <li>
                  <FiCheckCircle className="check-icon" />
                  <span>Support technique réactif</span>
                </li>
                <li>
                  <FiCheckCircle className="check-icon" />
                  <span>Mises à jour régulières et gratuites</span>
                </li>
              </ul>
            </div>
            <div className="benefits-image">
              <div className="benefit-highlight">
                <FiHeart size={64} className="benefit-icon" />
                <h3>Fait pour les professionnels de santé marocains</h3>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <h2>Prêt à moderniser votre cabinet ?</h2>
            <p>Rejoignez des centaines de médecins qui font confiance à Wiqaytna Pro</p>
            <button onClick={() => navigate('/register')} className="btn-primary btn-large">
              Créer un compte gratuit
            </button>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="landing-footer">
        <div className="container">
          <div className="footer-content">
            <div className="footer-brand">
              <div className="logo">
                <FiHeart size={24} className="logo-icon" />
                <h3>Wiqaytna Pro</h3>
              </div>
              <p>La solution de gestion médicale pour le Maroc</p>
            </div>
            <div className="footer-links">
              <div className="footer-column">
                <h4>Produit</h4>
                <a href="#features">Fonctionnalités</a>
                <a href="#pricing">Tarifs</a>
                <a href="#faq">FAQ</a>
              </div>
              <div className="footer-column">
                <h4>Entreprise</h4>
                <a href="#about">À propos</a>
                <a href="#contact">Contact</a>
                <a href="#privacy">Confidentialité</a>
              </div>
              <div className="footer-column">
                <h4>Ressources</h4>
                <a href="#docs">Documentation</a>
                <a href="#support">Support</a>
                <a href="#blog">Blog</a>
              </div>
            </div>
          </div>
          <div className="footer-bottom">
            <p>&copy; 2024 Wiqaytna Pro. Tous droits réservés.</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Landing;
