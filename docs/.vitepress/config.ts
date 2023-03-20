import {defineConfig} from 'vitepress'

// https://vitepress.dev/reference/site-config
export const META_URL = 'https://kvalidator.knusperleicht.org'
export const META_TITLE = 'KValidator'
export const META_DESCRIPTION = 'A Kotlin DSL Validator'

export default defineConfig({
    title: "KValidator",
    description: META_DESCRIPTION,
    head: [
        ['meta', {property: 'og:url', content: META_URL}],
        ['meta', {property: 'og:description', content: META_DESCRIPTION}],
        ['meta', {property: 'twitter:url', content: META_URL}],
        ['meta', {property: 'twitter:title', content: META_TITLE}],
        ['meta', {property: 'twitter:description', content: META_DESCRIPTION}],
        ['link', {rel: 'icon', type: 'image/svg+xml', href: '/logo.svg'}],
    ],
    themeConfig: {
        logo: '/logo.svg',
        nav: [
            {text: 'Guide', link: '/core-concepts/', activeMatch: '^/core-concepts/'},
            {text: 'API', link: '/api/', activeMatch: '^/api/'}
        ],

        sidebar: {
            '/': [
                {
                    text: 'Introduction',
                    items: [
                        {
                            text: 'What is KValidator?',
                            link: '/introduction.html',
                        },
                        {
                            text: 'Getting Started',
                            link: '/getting-started.html',
                        },
                    ],
                },
                {
                    text: 'Core Concepts',
                    items: [
                        {text: 'Defining a validator', link: '/core-concepts/'},
                        {text: 'Custom constraint', link: '/core-concepts/'},
                        {text: 'Internationalization', link: '/core-concepts/'},
                        {text: 'SelfValidating data classes', link: '/core-concepts/'},
                    ],
                },
            ]
        },
        socialLinks: [
            {icon: 'github', link: 'https://github.com/knusperleicht/kvalidator'}
        ],

        footer: {
            copyright: 'Copyright © 2023 KNUSPERLEICHT DEVELOPMENT OG',
            message: 'Released under the MIT License.',
        }
    }
})
