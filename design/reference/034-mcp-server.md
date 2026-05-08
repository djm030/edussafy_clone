# MCP Server

Model Context Protocol (MCP) server for PrimeVue component library. Provides AI assistants with comprehensive access to PrimeVue component documentation.

## Claude Code

Add the PrimeVue MCP server using the CLI. After adding, start a new session and use /mcp to verify the connection.

## Cursor

Create .cursor/mcp.json in your project or ~/.cursor/mcp.json for global configuration.

```vue
{
    "mcpServers": {
        "primevue": {
            "command": "npx",
            "args": ["-y", "@primevue/mcp"]
        }
    }
}
```

## Example Prompts

Once installed, try asking your AI assistant:

```vue
"What are all the props available for the DataTable component?"
"Show me how to implement row selection in DataTable"
"How do I customize Button styles using Pass Through?"
"What design tokens are available for the Card component?"
"Find me a component for selecting multiple items from a list"
"Compare AutoComplete and Select components"
```

## Introduction

Model Context Protocol (MCP) is an open standard that enables AI models to connect with external tools and data sources . The PrimeVue MCP server provides AI assistants with comprehensive access to: Component documentation including props , events , slots , and methods Theming and styling with Pass Through and design tokens Code examples and usage patterns Accessibility information Installation and configuration guides

## OpenAI Codex

Add the PrimeVue MCP server using the CLI or edit ~/.codex/config.toml directly.

```vue
