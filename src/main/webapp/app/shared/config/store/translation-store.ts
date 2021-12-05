import { Module } from 'vuex';

export const translationStore: Module<any, any> = {
  state: {
    currentLanguage: localStorage.getItem('currentLanguage') || 'en',
    languages: {
      da: { name: 'Dansk' },
      en: { name: 'English' },
      fr: { name: 'Français' },
      de: { name: 'Deutsch' },
      it: { name: 'Italiano' },
      'pt-pt': { name: 'Português' },
      ru: { name: 'Русский' },
      es: { name: 'Español' },
      sv: { name: 'Svenska' },
      // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
    },
  },
  getters: {
    currentLanguage: state => state.currentLanguage,
    languages: state => state.languages,
  },
  mutations: {
    currentLanguage(state, newLanguage) {
      state.currentLanguage = newLanguage;
      localStorage.setItem('currentLanguage', newLanguage);
    },
  },
};
