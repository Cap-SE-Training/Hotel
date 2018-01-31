function googleTranslateElementInit() {
    new google.translate.TranslateElement({
    pageLanguage: 'en', includedLanguages: 'de,en,fr,nl',
    layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false
}, 'google_translate_element');
}
